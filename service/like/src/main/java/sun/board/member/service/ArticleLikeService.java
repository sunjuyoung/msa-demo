package sun.board.member.service;

import sun.board.common.event.EventType;
import sun.board.common.event.payload.ArticleLikedEventPayload;
import sun.board.common.event.payload.ArticleUnlikedEventPayload;
import sun.board.common.outboxmessagerelay.OutboxEventPublisher;
import sun.board.common.snowflake.Snowflake;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sun.board.member.entity.Likes;
import sun.board.member.entity.LikeCounts;
import sun.board.member.entity.TargetType;
import sun.board.member.repository.ArticleLikeCountRepository;
import sun.board.member.repository.ArticleLikeRepository;
import sun.board.member.service.response.ArticleLikeResponse;

@Service
@RequiredArgsConstructor
public class ArticleLikeService {

    private final Snowflake snowflake = new Snowflake();

    private final ArticleLikeRepository articleLikeRepository;

    private final ArticleLikeCountRepository articleLikeCountRepository;

    private  final OutboxEventPublisher outboxEventPublisher;

    public ArticleLikeResponse read(Long articleId, Long userId){
        return articleLikeRepository.findByTargetIdAndUserId(articleId, userId)
                .map(ArticleLikeResponse::from)
                .orElseThrow();
    }

    /**
     * 비관적 락을 사용한 좋아요 update 바로 사용
     * @param articleId
     * @param userId
     */
    @Transactional
    public void likePessmisticLock(Long articleId, Long userId){
        Likes likes = articleLikeRepository.save(Likes.create(snowflake.nextId(), articleId, userId, TargetType.ARTICLE));

        int result = articleLikeCountRepository.increaseLikeCount(articleId);
        if(result == 0){
            //최초 요청시 update가 안되고 1로 초기화
            //게시글 생성 시점에 미리 0으로 초기화 해두면 좋을 것 같다.
            articleLikeCountRepository.save(LikeCounts.init(articleId,1L));
        }

        outboxEventPublisher.publish(
                EventType.ARTICLE_LIKED,
                ArticleLikedEventPayload.builder()
                        .articleId(articleId)
                        .articleLikeId(likes.getLikeId())
                        .userId(likes.getUserId())
                        .createdAt(likes.getCreatedAt())
                        .articleLikeCount(count(articleId))
                        .build(),
                likes.getTargetId()
        );

    }

    @Transactional
    public void unlikePessmisticLock(Long articleId, Long userId){
        articleLikeRepository.findByTargetIdAndUserId(articleId, userId)
                .ifPresent((likes) -> {
                    articleLikeRepository.delete(likes);
                    articleLikeCountRepository.decreaseLikeCount(articleId);

                    outboxEventPublisher.publish(
                            EventType.ARTICLE_UNLIKED,
                            ArticleUnlikedEventPayload.builder()
                                    .articleId(articleId)
                                    .articleLikeId(likes.getLikeId())
                                    .userId(likes.getUserId())
                                    .createdAt(likes.getCreatedAt())
                                    .articleLikeCount(count(articleId))
                                    .build(),
                            likes.getTargetId()
                    );
                });
    }


    /**
     * 비관적 락을 사용한 좋아요 select ... for update + update
     * @param articleId
     * @param userId
     */
    @Transactional
    public void likePessmisticLock2(Long articleId, Long userId){
        articleLikeRepository.save(Likes.create(snowflake.nextId(), articleId, userId,TargetType.ARTICLE));

        LikeCounts likeCounts = articleLikeCountRepository.findLockedByTargetId(articleId)
                .orElseGet(() -> LikeCounts.init(articleId, 0L));

        likeCounts.increase();
        articleLikeCountRepository.save(likeCounts);

    }

    @Transactional
    public void unlikePessmisticLock2(Long articleId, Long userId){
        articleLikeRepository.findByTargetIdAndUserId(articleId, userId)
                .ifPresent((likes) -> {
                    articleLikeRepository.delete(likes);
                    LikeCounts likeCounts = articleLikeCountRepository.findLockedByTargetId(articleId)
                                    .orElseThrow();
                    likeCounts.decrease();
                });
    }


    /**
     * 낙관적 락을 사용한 좋아요
     * @param articleId
     * @param userId
     */
    @Transactional
    public void likeOptimisticLock(Long articleId, Long userId){
        articleLikeRepository.save(Likes.create(snowflake.nextId(), articleId, userId, TargetType.ARTICLE));

        LikeCounts likeCounts = articleLikeCountRepository.findById(articleId)
                .orElseGet(() -> LikeCounts.init(articleId, 0L));

        likeCounts.increase();
        articleLikeCountRepository.save(likeCounts);
    }

    @Transactional
    public void unlikeOptimisticLock(Long articleId, Long userId){
        articleLikeRepository.findByTargetIdAndUserId(articleId, userId)
                .ifPresent((likes) -> {
                    articleLikeRepository.delete(likes);
                    LikeCounts likeCounts = articleLikeCountRepository.findById(articleId).orElseThrow();
                    likeCounts.decrease();

                });
    }

    public Long count(Long articleId) {
        return articleLikeCountRepository.findById(articleId)
                .map(LikeCounts::getLikeCount)
                .orElse(0L);    //게시글 좋아요가 없을 경우 0 리턴
    }
}

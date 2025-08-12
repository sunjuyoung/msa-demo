package sun.board.comment.service;

import sun.board.common.event.EventType;
import sun.board.common.event.payload.CommentCreatedEventPayload;
import sun.board.common.event.payload.CommentDeletedEventPayload;
import sun.board.common.outboxmessagerelay.OutboxEventPublisher;
import sun.board.common.snowflake.Snowflake;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sun.board.comment.entity.ArticleCommentCount;
import sun.board.comment.entity.Comment;
import sun.board.comment.repository.ArticleCommentCountRepository;
import sun.board.comment.repository.CommentRepository;
import sun.board.comment.service.request.CommentCreateRequest;
import sun.board.comment.service.response.CommentPageResponse;
import sun.board.comment.service.response.CommentResponse;

import java.util.List;

import static java.util.function.Predicate.not;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final Snowflake snowflake = new Snowflake();

    private final ArticleCommentCountRepository articleCommentCountRepository;
    private  final OutboxEventPublisher outboxEventPublisher;

    @Transactional
    public CommentResponse create(CommentCreateRequest request){

        Comment parent = findParent(request);
        Comment saveComment = commentRepository.save(
                Comment.create(
                        snowflake.nextId(),
                        request.getContent(),
                        parent == null ? null : request.getParentCommentId(),
                        request.getArticleId(),
                        request.getWriterId()
                )
        );

        int result = articleCommentCountRepository.increaseCommentCount(request.getArticleId());
        if(result == 0){
            articleCommentCountRepository.save(
                    ArticleCommentCount.init(request.getArticleId(), 1L)
            );
        }

        outboxEventPublisher.publish(
                EventType.COMMENT_CREATED,
                CommentCreatedEventPayload.builder()
                        .commentId(saveComment.getCommentId())
                        .articleId(saveComment.getArticleId())
                        .writerId(saveComment.getWriterId())
                        .content(saveComment.getContent())
                        .deleted(saveComment.getDeleted())
                        .parentCommentId(saveComment.getParentCommentId())
                        .createdAt(saveComment.getCreatedAt())
                        .articleCommentCount(count(saveComment.getArticleId()))
                        .build(),
                saveComment.getArticleId()
        );



        return CommentResponse.from(saveComment);
    }


    private Comment findParent(CommentCreateRequest request) {
        Long parentCommentId = request.getParentCommentId();
        if ( parentCommentId == null) {
            return null;
        }
        return commentRepository.findById(parentCommentId)
                .filter(not(Comment::getDeleted))
                .filter(Comment::isRoot)
                .orElseThrow();
    }

    public CommentResponse read(Long commnetId){
        Comment comment = commentRepository.findById(commnetId).orElseThrow();
        return CommentResponse.from(comment);
    }

    @Transactional
    public void delete(Long commentId){
        commentRepository.findById(commentId)
                .filter(not(Comment::getDeleted))
                .ifPresent(comment -> {
                    if(hasChildren(comment)){ //자식이 있으면 delete 표시만
                        comment.delete();
                    }else {
                        deleteComment(comment);
                    }

                    outboxEventPublisher.publish(
                            EventType.COMMENT_DELETED,
                            CommentDeletedEventPayload.builder()
                                    .commentId(comment.getCommentId())
                                    .articleId(comment.getArticleId())
                                    .writerId(comment.getWriterId())
                                    .content(comment.getContent())
                                    .deleted(comment.getDeleted())
                                    .createdAt(comment.getCreatedAt())
                                    .articleCommentCount(count(comment.getArticleId()))
                                    .build(),
                            comment.getArticleId()
                    );


                });


    }

    //자기 자신포함 댓글 수 확인 리밋을 걸어 2개만 확인
    private boolean hasChildren(Comment comment) {
        return commentRepository.countBy(comment.getArticleId(), comment.getCommentId(), 2L) == 2;
    }

    private void deleteComment(Comment comment) {
        commentRepository.delete(comment);
        articleCommentCountRepository.decreaseCommentCount(comment.getArticleId());

        //하위 댓글이 삭제 됬을때 상위 댓글이 삭제 표시된 상태면 모두 삭제
        if(!comment.isRoot()){
            commentRepository.findById(comment.getParentCommentId())
                    .filter(Comment::getDeleted) //삭제하기전 삭제 표시된 부모가 있으면 같이 삭제한다
                    .filter(not(this::hasChildren))
                    .ifPresent(this::deleteComment);
        }
    }

    public CommentPageResponse readAll(Long articleId, Long page, Long pageSize){
        List<CommentResponse> commentResponses = commentRepository.findAll(articleId, (page - 1) * pageSize, pageSize)
                .stream()
                .map(CommentResponse::from)
                .toList();

        Long count = commentRepository.count(articleId, PageCalculator.calculatePage(page, pageSize, 10L));

        return CommentPageResponse.of(commentResponses,count);
    }

    public List<CommentResponse> readAll(Long articleId, Long lastParentCommentId, Long lastCommentId,Long limit){
       List<Comment> comments = lastParentCommentId == null || lastCommentId ==null ?
                commentRepository.findAllInfiniteScroll(articleId,limit) :
                commentRepository.findAllInfiniteScroll(articleId,lastParentCommentId,lastCommentId,limit);

       return comments.stream().map(CommentResponse::from).toList();
    }

    public Long count(Long articleId){
        return articleCommentCountRepository.findById(articleId)
                .map(ArticleCommentCount::getCommentCount)
                .orElse(0L);
    }
}

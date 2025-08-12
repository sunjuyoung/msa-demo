package sun.board.view.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sun.board.common.event.EventType;
import sun.board.common.event.payload.ArticleViewedEventPayload;
import sun.board.common.outboxmessagerelay.OutboxEventPublisher;
import sun.board.view.entity.TargetType;
import sun.board.view.entity.ViewCount;
import sun.board.view.repository.ViewCountBackUpRepository;

@Service
@RequiredArgsConstructor
public class ArticleViewCountBackupProcessor {

    private final ViewCountBackUpRepository viewCountBackUpRepository;
    private  final OutboxEventPublisher outboxEventPublisher;

    @Transactional
    public void backup(Long aritcleId, Long viewCount){
        int result = viewCountBackUpRepository.updateArticleViewCount(aritcleId, TargetType.ARTICLE, viewCount);
        if(result == 0){
            viewCountBackUpRepository.save(ViewCount.create(aritcleId, viewCount, TargetType.ARTICLE));
        }
        outboxEventPublisher.publish(
                EventType.ARTICLE_VIEWED,
                ArticleViewedEventPayload.builder()
                        .articleId(aritcleId)
                        .articleViewCount(viewCount)
                        .build(),
                aritcleId
        );
    }
}

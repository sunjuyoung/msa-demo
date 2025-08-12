package sun.board.hotarticle.service;

import lombok.extern.slf4j.Slf4j;
import sun.board.common.event.Event;
import sun.board.common.event.EventPayload;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import sun.board.hotarticle.repository.ArticleCreatedTimeRepository;
import sun.board.hotarticle.repository.HotArticleListRepository;
import sun.board.hotarticle.service.eventHandler.EventHandler;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class HotArticleScoreUpdater {

    private final HotArticleListRepository hotArticleListRepository;
    private final HotArticleScoreCalculator hotArticleScoreCalculator;
    private final ArticleCreatedTimeRepository articleCreatedTimeRepository;

    private static final long HOT_ARTICLE_COUNT = 10;
    private static final Duration HOT_ARTICLE_TTL = Duration.ofDays(7);

    public void update(Event<EventPayload> event, EventHandler<EventPayload> eventHandler) {
        Long articleId = eventHandler.findArticleId(event);
        //생성시간 확인
        log.info("[MessageRelay.publishPendingEvent] articleId={}", articleId);
        LocalDateTime createdTime = articleCreatedTimeRepository.read(articleId);

        log.info("createdTime : {}",createdTime);


        //생성시간이 오늘이 아니라면 return
        if(!isArticleCreatedToday(createdTime)){
            return;
        }
        eventHandler.handle(event);

        //점수 계산
        long score = hotArticleScoreCalculator.calculate(articleId);


        //점수 업데이트
        hotArticleListRepository.add(
                articleId,
                createdTime,
                score,
                HOT_ARTICLE_COUNT,
                HOT_ARTICLE_TTL
        );

    }

    private boolean isArticleCreatedToday(LocalDateTime createdTime) {
        return createdTime != null && createdTime.toLocalDate().equals(LocalDate.now());
    }
}

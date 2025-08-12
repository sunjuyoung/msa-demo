package sun.board.articleread.consumer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;
import sun.board.articleread.service.ArticleReadService;
import sun.board.common.event.Event;
import sun.board.common.event.EventPayload;
import sun.board.common.event.EventType;

@Slf4j
@Component
@RequiredArgsConstructor
public class ArticleReadEventConsumer {
    private final ArticleReadService articleReadService;

    @KafkaListener(topics = {
            EventType.Topic.SUN_BOARD_ARTICLE,
            EventType.Topic.SUN_BOARD_COMMENT,
            EventType.Topic.SUN_BOARD_LIKE
    })
    public void listen(String message, Acknowledgment ack) {
        log.info("[ArticleReadEventConsumer.listen] message={}", message);
        Event<EventPayload> event = Event.fromJson(message);
        if (event != null) {
            articleReadService.handlEvent(event);
        }
        ack.acknowledge();
    }
}

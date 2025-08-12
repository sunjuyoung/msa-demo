package sun.board.hotarticle.consumer;

import sun.board.common.event.Event;
import sun.board.common.event.EventPayload;
import sun.board.common.event.EventType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;
import sun.board.hotarticle.service.HotArticleService;

@Slf4j
@Component
@RequiredArgsConstructor
public class HotArticleEventConsumer {

    private final HotArticleService hotArticleService;

    @KafkaListener(topics = {
            EventType.Topic.SUN_BOARD_ARTICLE,
            EventType.Topic.SUN_BOARD_COMMENT,
            EventType.Topic.SUN_BOARD_LIKE,
            EventType.Topic.SUN_BOARD_VIEW
    })
    public void listen(String message, Acknowledgment ack) {
          log.info("hotarticleconsumer message={}", message);
        Event<EventPayload> event = Event.fromJson(message);
        if(event != null){
            hotArticleService.handleEvent(event);
        }
        //수신 확인 카프카에게 전달
        ack.acknowledge();


    }
}

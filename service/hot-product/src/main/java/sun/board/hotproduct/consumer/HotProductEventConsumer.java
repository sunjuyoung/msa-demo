package sun.board.hotproduct.consumer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;
import sun.board.hotproduct.event.Event;
import sun.board.hotproduct.event.EventPayload;
import sun.board.hotproduct.event.EventType;
import sun.board.hotproduct.service.HotProductService;

@Slf4j
@Component
@RequiredArgsConstructor
public class HotProductEventConsumer {

    private final HotProductService hotProductService;

    @KafkaListener(topics = {
            EventType.Topic.PRODUCT_VIEW,
            EventType.Topic.ARTICLE_VIEW
    })
    public void listen(String message , Acknowledgment ack){
        log.info("hotproductconsumer message={}",message);

        Event<EventPayload> event = Event.fromJson(message);
        if(event != null){
            //핸들링 로직
            log.info("event={}",event);
            hotProductService.handlerEvent(event);
        }
        //수신 확인 카프카에게 전달
        ack.acknowledge();
    }
}

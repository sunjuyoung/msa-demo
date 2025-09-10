package sun.board.view.outbox;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.concurrent.TimeUnit;

@Slf4j
@Component
@RequiredArgsConstructor
public class MessageRelay {

    private final OutBoxRepository outBoxRepository;
    private final KafkaTemplate<String, String> kafkaTemplate;

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void createOutBox(OutBox outBox) {
        log.info("MessageRelay createOutBox called: {}", outBox);
        outBoxRepository.save(outBox);
    }

    @Async("messageRelayPublishExecutor")
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void publishOutBox(OutBox outBox) {
        log.info("MessageRelay publishOutBox called: {}", outBox);
        publishEvent(outBox);
    }

    private void publishEvent(OutBox outbox){
        try {
            kafkaTemplate.send(outbox.getEventType().getTopic(), outbox.getPayload())
                    .get(1, TimeUnit.SECONDS);
            outBoxRepository.delete(outbox);
        }catch (Exception e){
            log.error("[MessageRelay.publishEvent] outbox={}", outbox, e);

        }
    }
}

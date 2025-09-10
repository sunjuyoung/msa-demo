package sun.board.common.outboxmessagerelay;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;
@Slf4j
@Component
@RequiredArgsConstructor
public class MessageRelay {
    private final OutboxRepository outboxRepository;
    private final MessageRelayCoordinator messageRelayCoordinator;

    //카프카로 이벤트 전송
    private final KafkaTemplate<String, String> messageRelayKafkaTemplate;

    //트랜잭션 이벤트 발생시 outbox에 저장
    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void createOutbox(OutboxEvent outboxEvent) {
        log.info("[MessageRelay.createOutbox] outboxEvent={}", outboxEvent);
        outboxRepository.save(outboxEvent.getOutbox());
    }

    //비동기로 카프카로 이벤트 전송
    @Async("messageRelayPublishExecutor")//MessageRelayConfig.java의 Executor Bean
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)//트랜잭션 커밋완료 후 이벤트 발행은 비동기로
    public void publishEvent(OutboxEvent outboxEvent) {
        publishEvent(outboxEvent.getOutbox());
    }

    private void publishEvent(Outbox outbox) {
        try {
            messageRelayKafkaTemplate.send(
                    outbox.getEventType().getTopic(),
                    String.valueOf(outbox.getShardKey()),
                    outbox.getPayload()
            ).get(1,TimeUnit.SECONDS);
            outboxRepository.delete(outbox);
        } catch (Exception e) {
            log.error("[MessageRelay.publishEvent] outbox={}", outbox, e);
        }
    }

    //outbox데이터베이스 남아있는 미전송 이벤트 10초마다 주기적으로 polling 후 전송
//    @Scheduled(
//            fixedDelay = 10,
//            initialDelay = 5,
//            timeUnit = TimeUnit.SECONDS,
//            scheduler = "messageRelaypublishPendingExecutor" //MessageRelayConfig.java의 Executor Bean 싱글스레드
//    )
//    public void publishPendingEvent() {
//        AssignedShard assignedShard = messageRelayCoordinator.assignShards();
//        log.info("[MessageRelay.publishPendingEvent] assignedShard size={}", assignedShard.getShards().size());
//        for (Long shard : assignedShard.getShards()) {
//            List<Outbox> outboxes = outboxRepository.findAllByShardKeyAndCreatedAtLessThanEqualOrderByCreatedAtAsc(
//                    shard,
//                    LocalDateTime.now().minusSeconds(10),
//                    Pageable.ofSize(100)
//            );
//            for (Outbox outbox : outboxes) {
//                publishEvent(outbox);
//            }
//        }
//    }
}

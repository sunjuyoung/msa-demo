package sun.board.ordering.consumer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;
import sun.board.common.dataserializer.DataSerializer;
import sun.board.ordering.dto.StockEvent;
import sun.board.ordering.service.OrderingService;

@Slf4j
@Component
@RequiredArgsConstructor
public class StockStatusConsumer {

    private final OrderingService orderService; // 주문 상태를 업데이트할 서비스

    private static final String STOCK_STATUS_TOPIC = "stock-status-topic";

    @KafkaListener(topics = STOCK_STATUS_TOPIC, containerFactory = "kafkaListenerContainerFactory")
    public void stockStatusConsumer(String message, Acknowledgment ack) {
        try {
            StockEvent event = DataSerializer.deserialize(message, StockEvent.class);
            log.info("재고 상태 이벤트 수신: orderId = {}, productId={}, status={}",event.getOrderId(), event.getProductId(), event.getStatus());

            switch (event.getStatus()) {
                case "STOCK_INSUFFICIENT":
                    // 주문 취소 처리
                   // orderService.cancelOrderByProductId(event);
                    break;
                case "SYSTEM_ERROR":
                    // 주문 대기 상태로 두거나 관리자 확인 필요
                    log.info("시스템 오류로 인해 주문 상태를 대기 중으로 유지: productId={}", event.getProductId());
                    break;
                default:
                    log.warn("알 수 없는 상태 이벤트: {}", event.getStatus());
            }

            ack.acknowledge(); // 정상 처리 후 오프셋 커밋
        } catch (Exception e) {
            log.error("StockStatusConsumer 처리 중 오류 발생: {}", e.getMessage(), e);
            // 예외 발생 시 ack 생략 → 재처리 or DLQ로 이동
        }
    }
}
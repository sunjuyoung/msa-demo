package sun.board.product.consumer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import sun.board.common.dataserializer.DataSerializer;
import sun.board.product.dto.ProductUpdateStockDto;
import sun.board.product.dto.StockEvent;
import sun.board.product.exception.ex.LockAcquisitionFailedException;
import sun.board.product.exception.ex.StockInsufficientException;
import sun.board.product.service.ProductService;
import sun.board.product.service.RedissonLockStock;

@Slf4j
@Component
@RequiredArgsConstructor
public class ProductStockConsumer {

    private final RedissonLockStock redissonLockStock;

    private final String DECREASE_STOCK_TOPIC = "decrease-stock-topic"; // 카프카 토픽 이름
    private final String STOCK_STATUS_TOPIC = "stock-status-topic";


    @KafkaListener(topics = DECREASE_STOCK_TOPIC , containerFactory = "kafkaListenerContainerFactory")
    public void stockConsumer(String message) {
        ProductUpdateStockDto dto = DataSerializer.deserialize(message, ProductUpdateStockDto.class);

        try {
            redissonLockStock.decreaseStock(dto);
            // 성공 시 다음 단계를 위한 이벤트 발행 (선택사항, 보통은 다음 서비스에서 바로 받음)
        } catch (StockInsufficientException e) {
            log.warn("재고 부족: {}", e.getMessage());
           // kafkaTemplate.send(STOCK_STATUS_TOPIC, new StockEvent(dto.getProductId(), "STOCK_INSUFFICIENT"));
        } catch (LockAcquisitionFailedException e) {
            log.error("분산 락 획득 실패: {}", e.getMessage());
            //kafkaTemplate.send(STOCK_STATUS_TOPIC, new StockEvent(dto.getProductId(), "SYSTEM_ERROR"));
        } catch (Exception e) {
            log.error("알 수 없는 에러 발생: {}", e.getMessage());
           // kafkaTemplate.send(STOCK_STATUS_TOPIC, new StockEvent(dto.getProductId(), "SYSTEM_ERROR"));
        }


    }

}

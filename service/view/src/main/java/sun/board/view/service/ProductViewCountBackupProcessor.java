package sun.board.view.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sun.board.view.entity.TargetType;
import sun.board.view.entity.ViewCount;
import sun.board.view.event.EventType;
import sun.board.view.event.payload.ProductViewedEventPayload;
import sun.board.view.outbox.OutBoxEventPublisher;
import sun.board.view.repository.ViewCountBackUpRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductViewCountBackupProcessor {

    private final ViewCountBackUpRepository  viewCountBackUpRepository;
    private final OutBoxEventPublisher outBoxEventPublisher;

    @Transactional
    public void backup(Long productOptionId, Long viewCount){
        log.info("ProductViewCountBackupProcessor backup called: productOptionId={}, viewCount={}", productOptionId, viewCount);
        int result = viewCountBackUpRepository.updateProductViewCount(productOptionId, TargetType.PRODUCT.name(), viewCount);
        if(result == 0){
            log.info( "Creating new ViewCount record for productOptionId={}", productOptionId);
            viewCountBackUpRepository.save(ViewCount.create(productOptionId, viewCount, TargetType.PRODUCT));
        }

        outBoxEventPublisher.publish(
                EventType.PRODUCT_VIEWED,
                ProductViewedEventPayload.builder()
                        .productOptionId(productOptionId)
                        .productViewCount(viewCount)
                        .build()
        );

    }
}

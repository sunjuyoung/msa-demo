package sun.board.hotproduct.service.eventHandler;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import sun.board.hotproduct.event.Event;
import sun.board.hotproduct.event.EventType;
import sun.board.hotproduct.event.payload.ProductViewedEventPayload;
import sun.board.hotproduct.repository.ProductViewCountRepository;

@Component
@RequiredArgsConstructor
public class ProductViewEventHandler implements  EventHandler<ProductViewedEventPayload> {

    private final ProductViewCountRepository productViewCountRepository;


    @Override
    public void handle(Event<ProductViewedEventPayload> event) {
        ProductViewedEventPayload payload = event.getPayload();
        productViewCountRepository.createOrUpdate(
                payload.getProductOptionId(),
                payload.getProductViewCount()
        );

    }

    @Override
    public boolean supports(Event<ProductViewedEventPayload> event) {
        return event.getType() == EventType.PRODUCT_VIEWED;
    }

    @Override
    public Long findProductId(Event<ProductViewedEventPayload> event) {
        return event.getPayload().getProductOptionId();
    }
}

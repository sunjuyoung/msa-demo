package sun.board.hotproduct.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;
import sun.board.hotproduct.dto.ProductResponse;
import sun.board.hotproduct.event.Event;
import sun.board.hotproduct.event.EventPayload;
import sun.board.hotproduct.service.eventHandler.EventHandler;
import sun.board.product.grpc.GetProductByProductOptionIdRequest;
import sun.board.product.grpc.GetProductResponse;
import sun.board.product.grpc.ProductServiceGrpc;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class HotProductService {

    private final List<EventHandler> evenHandlers;
    private final HotProductScoreCalculator hotProductScoreCalculator;
    private final HotProductListRepository hotProductListRepository;
    @GrpcClient("product-service")
    private ProductServiceGrpc.ProductServiceBlockingStub productStub;


    private static final long HOT_PRODUCT_LIST_COUNT = 10;
    private static final Duration HOT_PRODUCT_TTL = Duration.ofDays(5);


    public void handlerEvent(Event<EventPayload> event) {
        EventHandler<EventPayload> eventHandler = findEventHandler(event);
        if (eventHandler == null) {
            log.error("Unsupported event={}", event);
            return;
        }
        //
        eventHandler.handle(event);
        Long productOptionId = eventHandler.findProductId(event);

        long score = hotProductScoreCalculator.calculate(productOptionId);

        hotProductListRepository.add(
                productOptionId,
                LocalDateTime.now(),
                score,
                HOT_PRODUCT_LIST_COUNT,
                HOT_PRODUCT_TTL

        );

    }

    private EventHandler<EventPayload> findEventHandler(Event<EventPayload> event) {
        return evenHandlers.stream()
                .filter(e -> e.supports(event))
                .findAny()// parallel 스트림 순서 보장/조정이 불필요해서 더 빠를 수 있음.
                .orElse(null);
    }

    public List<ProductResponse> readAll(String dateStr) {
        //yyyyMMdd
        return hotProductListRepository.readAll(dateStr)
                .stream()
                .map(id -> {
                    GetProductByProductOptionIdRequest req = GetProductByProductOptionIdRequest.newBuilder()
                            .setProductOptionId(id)
                            .build();

                    GetProductResponse res = productStub.getProductByProductOptionId(req);
                    return ProductResponse.of(
                            res.getProduct().getId(),
                            res.getProduct().getName(),
                            res.getProduct().getDescription(),
                            (int) res.getProduct().getPrice(),
                            (int) res.getProduct().getStock(),
                            res.getProduct().getCategory(),
                            (int) res.getProduct().getSize()
                    );
                }).toList();
    }
}

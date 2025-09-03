package sun.board.ordering.service;

import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import sun.board.ordering.dto.*;
import sun.board.ordering.entity.OrderStatus;
import sun.board.ordering.entity.Ordering;
import sun.board.ordering.exception.ex.StockInsufficientException;
import sun.board.ordering.repository.OrderingRepository;
import sun.board.product.grpc.GetProductRequest;
import sun.board.product.grpc.GetProductResponse;
import sun.board.product.grpc.ProductServiceGrpc;

import java.util.List;

@Slf4j
@Service
public class OrderingService {

    private final WebClient productWebClient;
    private final OrderingRepository orderingRepository;
    private final ProductFeign productFeign;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @GrpcClient("product-service")
    private ProductServiceGrpc.ProductServiceBlockingStub productStub;

    private final String DECREASE_STOCK_TOPIC = "decrease-stock-topic"; // 카프카 토픽 이름

    public OrderingService(
            @Qualifier("productWebClient") WebClient productWebClient,
            OrderingRepository orderingRepository,
            ProductFeign productFeign,
            KafkaTemplate<String, Object> kafkaTemplate)
    {
        this.productWebClient = productWebClient;
        this.orderingRepository = orderingRepository;
        this.productFeign = productFeign;
        this.kafkaTemplate = kafkaTemplate;
    }


    @Transactional
    public OrderViewResponseDTO orderCreateFeign(OrderCreateDto dto){

        int quantity = dto.getProductCount();
        Long productId = dto.getProductId();
        Long productOptionId = dto.getProductOptionId();

        //상품 조회
        GetProductRequest request = GetProductRequest.newBuilder()
                .setProductId(productId)
                .setProductOptionId(productOptionId.intValue())
                .build();

        GetProductResponse response = productStub.getProduct(request);

        //주문 생성 PENDING
        Ordering ordering = Ordering.builder()
                .productId(dto.getProductId())
                .productOptionId(productOptionId)
                .quantity(quantity)
                .memberId(dto.getUserId())
                .orderStatus(OrderStatus.PENDING)
                .totalPrice(dto.getTotalPrice())
                .build();


        Ordering saved = orderingRepository.save(ordering);

        // 재고 감소 로직 -> 결제 서비스로 이동

        OrderViewResponseDTO orderViewResponseDTO = OrderViewResponseDTO.create(
                productId,
                saved.getId(),
                dto.getTotalPrice(),
                response.getProduct().getName(),
                response.getProduct().getColor(),
                (int) response.getProduct().getSize(),
                quantity,
                productOptionId
        );

        return orderViewResponseDTO;
    }

    // userId로 주문 조회
    public OrderHistoryResponse getOrdersByUser(Long userId) {
        List<Ordering> orderingList = orderingRepository.findByMemberId(userId);

        OrderHistoryResponse orderHistoryResponse = new OrderHistoryResponse();

        orderingList.forEach(o -> {
            GetProductRequest request = GetProductRequest.newBuilder()
                    .setProductId(o.getProductId())
                    .setProductOptionId(o.getProductOptionId().intValue())
                    .build();
            GetProductResponse productResponse = productStub.getProduct(request);
            OrderItemDto orderItemDto = OrderItemDto.create(productResponse, o.getProductOptionId());
            OrderSummaryDto orderSummaryDto = OrderSummaryDto.create(o);
            orderSummaryDto.getItems().add(orderItemDto);
            orderHistoryResponse.getOrders().add(orderSummaryDto);
        });

        return orderHistoryResponse;

    }

    public void cancelOrder(Long orderId) {
    }

//    @Transactional
//    public Long orderCreate(OrderCreateDto dto, String userId){
//
//        int quantity = dto.getProductCount();
//        Long productId = dto.getProductId();
//        //상품 조회
//        ProductDto response = productWebClient.get()
//                .uri("/product/{id}", productId)
//                //.header("X-USER-ID", userId) // 사용자 ID를 헤더에 추가
//                .retrieve()
//                .bodyToMono(ProductDto.class)
//                .block();
//
//        if(response.getStockQuantity() < quantity){
//            throw new IllegalArgumentException("재고가 부족합니다.");
//        }
//            Ordering ordering = Ordering.builder()
//                    .productId(dto.getProductId())
//                    .quantity(dto.getProductCount())
//                    .memberId(Long.parseLong(userId))
//                    .orderStatus(OrderStatus.PENDING)
//                    .build();
//
//
//
//        // 재고 감소 로직
//        ProductUpdateStockDto productUpdateStockDto
//                = ProductUpdateStockDto.productUpdateStockDto(dto.getProductId(), dto.getProductCount());
//        Long result = productWebClient.put()
//                .uri("/product/decreaseStock")
//                .bodyValue(productUpdateStockDto) // ProductDto를 요청 본문에 포함
//                .retrieve()
//                .bodyToMono(Long.class)
//                .block();
//
//        Ordering saved = orderingRepository.save(ordering);
//
//        return saved.getId();
//    }






}

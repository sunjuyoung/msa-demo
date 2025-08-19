package sun.board.ordering.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import sun.board.ordering.dto.OrderCreateDto;
import sun.board.ordering.dto.ProductDto;
import sun.board.ordering.dto.ProductUpdateStockDto;
import sun.board.ordering.entity.OrderStatus;
import sun.board.ordering.entity.Ordering;
import sun.board.ordering.repository.OrderingRepository;

@Slf4j
@Service
public class OrderingService {

    private final WebClient productWebClient;
    private final OrderingRepository orderingRepository;
    private final ProductFeign productFeign;
    private final KafkaTemplate<String, Object> kafkaTemplate;

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
    public Long orderCreateFeign(OrderCreateDto dto, String userId){

        int quantity = dto.getProductCount();
        Long productId = dto.getProductId();
        //상품 조회

        ProductDto response = productFeign.getProductById(productId);

        if(response.getStockQuantity() < quantity){
            throw new IllegalArgumentException("재고가 부족합니다.");
        }
        Ordering ordering = Ordering.builder()
                .productId(dto.getProductId())
                .quantity(dto.getProductCount())
                .memberId(Long.parseLong(userId))
                .orderStatus(OrderStatus.PENDING)
                .build();



        // 재고 감소 로직
        ProductUpdateStockDto productUpdateStockDto
                = ProductUpdateStockDto.productUpdateStockDto(dto.getProductId(), dto.getProductCount());

        // 카프카에 재고 감소 요청 전송(비동기 방식)
        kafkaTemplate.send(DECREASE_STOCK_TOPIC,String.valueOf(productId), productUpdateStockDto);
        //  재고 감소를 위한 Feign 클라이언트 호출(동기 방식)
        //Long result = productFeign.decreaseStock(productUpdateStockDto);

        Ordering saved = orderingRepository.save(ordering);

        return saved.getId();
    }

    @Transactional
    public Long orderCreate(OrderCreateDto dto, String userId){

        int quantity = dto.getProductCount();
        Long productId = dto.getProductId();
        //상품 조회
        ProductDto response = productWebClient.get()
                .uri("/product/{id}", productId)
                //.header("X-USER-ID", userId) // 사용자 ID를 헤더에 추가
                .retrieve()
                .bodyToMono(ProductDto.class)
                .block();

        if(response.getStockQuantity() < quantity){
            throw new IllegalArgumentException("재고가 부족합니다.");
        }
            Ordering ordering = Ordering.builder()
                    .productId(dto.getProductId())
                    .quantity(dto.getProductCount())
                    .memberId(Long.parseLong(userId))
                    .orderStatus(OrderStatus.PENDING)
                    .build();



        // 재고 감소 로직
        ProductUpdateStockDto productUpdateStockDto
                = ProductUpdateStockDto.productUpdateStockDto(dto.getProductId(), dto.getProductCount());
        Long result = productWebClient.put()
                .uri("/product/decreaseStock")
                .bodyValue(productUpdateStockDto) // ProductDto를 요청 본문에 포함
                .retrieve()
                .bodyToMono(Long.class)
                .block();

        Ordering saved = orderingRepository.save(ordering);

        return saved.getId();
    }






}

package sun.board.ordering.dto;

import lombok.*;
import sun.board.product.grpc.GetProductResponse;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderItemDto {

    private Long productId;         // 상품 ID
    private String productName;     // 상품 이름
    private Integer price;          // 단가
    private Integer quantity;       // 수량

    private Long productOptionId;  // 옵션 ID
    private String color;          // 색상
    private String size;           // 사이즈

    public static OrderItemDto create(GetProductResponse response, Long productOptionId){
        return OrderItemDto.builder()
                .productId(response.getProduct().getId())
                .productName(response.getProduct().getName())
                .price((int) response.getProduct().getPrice())
                .quantity((int) response.getProduct().getStock())
                .productOptionId(productOptionId)
                .color(response.getProduct().getColor())
                .build();
    }
}

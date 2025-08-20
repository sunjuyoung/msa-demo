package sun.board.ordering.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductUpdateStockDto {
    private Long productId;
    private Integer stockQuantity;
    private Long orderId;


    public static ProductUpdateStockDto productUpdateStockDto(Long productId, Integer stockQuantity, Long orderId) {
        return ProductUpdateStockDto.builder()
                .orderId(orderId)
                .productId(productId)
                .stockQuantity(stockQuantity)
                .build();
    }

}

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


    public static ProductUpdateStockDto productUpdateStockDto(Long productId, Integer stockQuantity) {
        return ProductUpdateStockDto.builder()
                .productId(productId)
                .stockQuantity(stockQuantity)
                .build();
    }

}

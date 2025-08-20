package sun.board.product.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductUpdateStockDto {
    private Long productId;
    private Integer stockQuantity;
    private Long orderId; // 주문 ID, 주문 상태 변경 시 사용
}

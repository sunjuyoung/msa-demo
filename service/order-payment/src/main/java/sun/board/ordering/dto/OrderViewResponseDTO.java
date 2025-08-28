package sun.board.ordering.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderViewResponseDTO {

    private Long productId;
    private Long orderId;
    private BigDecimal amount;
    private String orderName;
    private String color;
    private int size;
    private Integer quantity;
}

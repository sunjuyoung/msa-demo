package sun.board.ordering.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderCreateDto {

    private Long productId;
    private Long productOptionId;
    private Integer productCount;
    private Long userId;
    private BigDecimal totalPrice;
    private String color;
    private int size;
}

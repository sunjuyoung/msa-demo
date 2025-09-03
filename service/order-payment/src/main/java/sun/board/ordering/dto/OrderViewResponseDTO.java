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
    private Long productOptionId;
    private Long orderId;
    private BigDecimal amount;
    private String orderName;
    private String color;
    private int size;
    private Integer quantity;

    //create
    public static OrderViewResponseDTO create(Long productId, Long orderId, BigDecimal amount, String orderName,
                                              String color, int size, Integer quantity, Long productOptionId) {
        return OrderViewResponseDTO.builder()
                .productId(productId)
                .orderId(orderId)
                .amount(amount)
                .orderName(orderName)
                .color(color)
                .size(size)
                .quantity(quantity)
                .productOptionId(productOptionId)
                .build();
    }
}

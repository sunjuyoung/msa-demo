package sun.board.ordering.dto;

import lombok.*;
import sun.board.ordering.entity.Ordering;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderSummaryDto {

    private Long orderId;               // 주문 ID
    private LocalDateTime orderDate;    // 주문일시
    private String status;              // 주문 상태 (CREATED, PAID, CANCELED 등)
    private Integer totalAmount;
    @Builder.Default// 총 결제 금액
    private List<OrderItemDto> items = new ArrayList<>();   // 주문 상품 목록


    //static factory method
    public static OrderSummaryDto create(Ordering ordering ){
        return OrderSummaryDto.builder()
                .orderId(ordering.getId())
                .orderDate(ordering.getCreatedAt())
                .status(ordering.getOrderStatus().name())
                .totalAmount(ordering.getTotalPrice().intValue())
                .build();
    }

}

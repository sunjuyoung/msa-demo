package sun.board.ordering.dto;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderHistoryResponse {
    @Builder.Default
    private List<OrderSummaryDto> orders = new ArrayList<>();
}

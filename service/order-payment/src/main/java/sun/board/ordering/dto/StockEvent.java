package sun.board.ordering.dto;

import lombok.Data;

@Data
public class StockEvent {

    //new StockEvent(dto.getProductId(), "STOCK_INSUFFICIENT"));
    private Long productId;
    private String status;
    private Long orderId;


}

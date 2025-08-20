package sun.board.product.dto;

import lombok.Data;

@Data
public class StockEvent {

//new StockEvent(dto.getProductId(), "STOCK_INSUFFICIENT"));
    private Long productId;
    private String status;
    private Long orderId;

    public StockEvent(Long productId,Long orderId, String status) {
        this.productId = productId;
        this.status = status;
        this.orderId = orderId;
    }
}

package sun.board.payment.dto.response;

import lombok.Data;

import java.util.List;

@Data
public class PendingPaymentEvent {

    private Long paymentEventId;
    private String paymentKey;
    private String orderKey;
    private List<PendingPaymentOrder> pendingPaymentOrders;

}

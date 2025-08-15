package sun.board.payment.dto.response;


import lombok.Data;
import sun.board.payment.entity.enums.PaymentOrderStatus;

@Data
public class PendingPaymentOrder {

    private Long paymentOrderId;
    private PaymentOrderStatus status;
    private Long amount;
    private int failedCount;
    private int threshold;
}

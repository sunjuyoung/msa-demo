package sun.board.payment.dto.request;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
public class PaymentConfirmCommand {

    private String paymentKey;
    private String orderId;
    private Long amount;


    public PaymentConfirmCommand(String paymentKey, String orderId, Long amount) {
        this.paymentKey = paymentKey;
        this.orderId = orderId;
        this.amount = amount;
    }
}

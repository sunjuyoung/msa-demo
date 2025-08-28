package sun.board.payment.exception.ex;

import lombok.Getter;

@Getter
public class PaymentAmountException extends RuntimeException {
    public PaymentAmountException(String message) {
        super(message);
    }
}

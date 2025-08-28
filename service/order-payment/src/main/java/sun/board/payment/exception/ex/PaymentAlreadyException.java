package sun.board.payment.exception.ex;

import lombok.Getter;

@Getter
public class PaymentAlreadyException extends RuntimeException {
    public PaymentAlreadyException(String message) {
        super(message);
    }
}

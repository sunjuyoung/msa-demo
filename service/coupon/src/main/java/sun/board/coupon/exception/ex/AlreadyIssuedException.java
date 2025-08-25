package sun.board.coupon.exception.ex;

import lombok.Getter;

@Getter
public class AlreadyIssuedException extends RuntimeException {
    public AlreadyIssuedException(String message) {
        super(message);
    }
}

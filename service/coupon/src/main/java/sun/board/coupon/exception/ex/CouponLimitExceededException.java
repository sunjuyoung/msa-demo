package sun.board.coupon.exception.ex;

import lombok.Getter;

@Getter
public class CouponLimitExceededException extends RuntimeException {
    public CouponLimitExceededException(String message) {
        super(message);
    }
}

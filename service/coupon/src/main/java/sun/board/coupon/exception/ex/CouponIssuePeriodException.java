package sun.board.coupon.exception.ex;

import lombok.Getter;

@Getter
public class CouponIssuePeriodException extends RuntimeException {
    public CouponIssuePeriodException(String message) {
        super(message);
    }
}

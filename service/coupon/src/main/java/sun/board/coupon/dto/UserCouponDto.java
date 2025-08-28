package sun.board.coupon.dto;

public interface UserCouponDto {
    Long getUserCouponId();
    Long getCouponId();
    String getCode();
    Integer getDiscountAmount();
    Integer getMinOrderAmount();
    String getCouponName();
}

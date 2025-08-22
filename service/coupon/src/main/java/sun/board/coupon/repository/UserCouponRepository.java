package sun.board.coupon.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sun.board.coupon.entity.UserCoupon;

public interface UserCouponRepository extends JpaRepository<UserCoupon,Long> {
    boolean existsByUserIdAndCoupon_Id(Long userId, Long couponId);
}

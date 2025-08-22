package sun.board.coupon.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sun.board.coupon.entity.Coupon;

public interface CouponRepository extends JpaRepository<Coupon,Long> {
}

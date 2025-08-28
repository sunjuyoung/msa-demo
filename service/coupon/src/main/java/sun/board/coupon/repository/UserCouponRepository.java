package sun.board.coupon.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import sun.board.coupon.dto.UserCouponDto;
import sun.board.coupon.entity.UserCoupon;

import java.util.List;

public interface UserCouponRepository extends JpaRepository<UserCoupon,Long> {
    boolean existsByUserIdAndCoupon_Id(Long userId, Long couponId);



    @Query(value = """
        SELECT 
            uc.id              AS userCouponId,
            c.id               AS couponId,
            c.code             AS code,
            c.discount_amount  AS discountAmount,
            c.min_order_amount AS minOrderAmount,
            c.coupon_name      AS couponName
        FROM user_coupon uc
        INNER JOIN coupon c ON uc.coupon_id = c.id
        WHERE uc.user_id = :userId
        """, nativeQuery = true)
    List<UserCouponDto> findCouponsByUserId(@Param("userId") Long userId);
}

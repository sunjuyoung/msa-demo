package sun.board.coupon.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sun.board.coupon.entity.Coupon;
import sun.board.coupon.entity.UserCoupon;
import sun.board.coupon.event.CouponIssueEvent;
import sun.board.coupon.exception.ex.AlreadyIssuedException;
import sun.board.coupon.exception.ex.CouponLimitExceededException;
import sun.board.coupon.repository.CouponRepository;
import sun.board.coupon.repository.UserCouponRepository;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CouponIssueAppService {
    private final CouponRepository couponRepository;
    private final CouponRedisService redisService;
    private final CouponIssueProducer producer;
    private final UserCouponRepository userCouponRepository;


    //회원가입 쿠폰 조회
    public Coupon getSignupCouponOrThrow() {
        return couponRepository.findById(1L)
                .orElseThrow(() -> new IllegalArgumentException("coupon not found"));
    }

    //중복 발급 check
    public void  isAlreadyIssued(Long couponId, Long userId) {
         if(userCouponRepository.existsByUserIdAndCoupon_Id(userId, couponId)){
             throw new AlreadyIssuedException("이미 발급된 쿠폰입니다.");
         }
    }

    //발급된 개수 조회 및 체크 메서드
    public void getIssuedCount(Coupon coupon) {
        long issued = redisService.incrIssuedCount(coupon.getId());
        if (issued > coupon.getTotalQuantity()) {
            // 초과 → 롤백
            redisService.decrIssuedCount(coupon.getId());
            throw new CouponLimitExceededException("coupon issued out of stock");
        }
    }


    //쿠폰 조회
    public Coupon getCouponOrThrow(Long couponId) {
        Coupon coupon = couponRepository.findById(couponId)
                .orElseThrow(() -> new IllegalArgumentException("coupon not found"));
        return coupon;
    }

    @Transactional
    public Long issueToUser(Long userId, Coupon coupon) {
        //db insert
        UserCoupon userCoupon = UserCoupon.builder()
                .userId(userId)
                .coupon(coupon)
                .used(false)
                .issuedAt(LocalDateTime.now())
                .build();
        UserCoupon save = userCouponRepository.save(userCoupon);
        return save.getId();
    }
}
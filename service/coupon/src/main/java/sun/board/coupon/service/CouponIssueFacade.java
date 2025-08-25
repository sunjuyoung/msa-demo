package sun.board.coupon.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import sun.board.coupon.entity.Coupon;
import sun.board.coupon.event.CouponIssueEvent;
import sun.board.coupon.exception.ex.CouponIssuePeriodException;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class CouponIssueFacade {

    private final CouponIssueAppService couponIssueAppService;
    private final CouponIssueProducer couponIssueProducer;

    public Long issueSignupCoupon(Long userId) {
        Coupon coupon = couponIssueAppService.getSignupCouponOrThrow();
        couponIssueAppService.isAlreadyIssued(coupon.getId(), userId);

        //알림

        return couponIssueAppService.issueToUser(userId, coupon);
    }

    public void issueEventCoupon(Long userId, Long couponId) {
        Coupon coupon = couponIssueAppService.getCouponOrThrow(couponId);
        //기간 체크
        if(coupon.getStartDate().isAfter(LocalDateTime.now()) || coupon.getEndDate().isBefore(LocalDateTime.now())){
            throw new CouponIssuePeriodException("쿠폰 발급 기간이 아닙니다.");
        }
        //개수 체크
        couponIssueAppService.getIssuedCount(coupon);

        CouponIssueEvent event = CouponIssueEvent.builder()
                .couponId(couponId)
                .userId(userId)
                .couponType(coupon.getType())
                .requestedAt(LocalDateTime.now())
                .build();

        try {
            couponIssueProducer.publish(event);
        }catch (Exception e){
            log.info( "쿠폰 발급 이벤트 전송 실패: {}", e.getMessage() );
        }
    }


}

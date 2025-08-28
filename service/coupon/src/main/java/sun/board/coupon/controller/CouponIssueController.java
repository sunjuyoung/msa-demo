package sun.board.coupon.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sun.board.coupon.dto.ApiResponse;
import sun.board.coupon.service.CouponIssueAppService;
import sun.board.coupon.service.CouponIssueFacade;

@RestController
@RequiredArgsConstructor
@RequestMapping("/coupons")
public class CouponIssueController {
    private final CouponIssueFacade  couponIssueFacade;
    private final CouponIssueAppService couponIssueAppService;

    @PostMapping("/{couponId}/issue/event")
    public ResponseEntity<ApiResponse> issueEventCoupon(@PathVariable Long couponId, @RequestParam("userId") Long userId) {
         couponIssueFacade.issueEventCoupon(userId, couponId);
        return ResponseEntity.ok(ApiResponse.success(couponId));
    }

    @PostMapping("/issue/signup")
    public ResponseEntity<ApiResponse> issueSignupCoupon(@RequestParam("userId") Long userId) {
       couponIssueFacade.issueSignupCoupon(userId);
        return ResponseEntity.ok(ApiResponse.success(userId));
    }
    //쿠폰 확인 By userId
    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse> getUserCoupons(@PathVariable Long userId) {
        return ResponseEntity.ok(ApiResponse.success(couponIssueAppService.getUserCoupons(userId)));
    }
}

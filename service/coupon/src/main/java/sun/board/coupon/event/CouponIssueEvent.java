package sun.board.coupon.event;

import lombok.*;
import sun.board.coupon.entity.CouponType;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CouponIssueEvent {
    private Long couponId;
    private Long userId;
    private CouponType couponType;
    private LocalDateTime requestedAt;
    private String requestId; // idempotency 보조(선택)
}
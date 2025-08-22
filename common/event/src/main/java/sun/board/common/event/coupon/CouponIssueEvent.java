package sun.board.common.event.coupon;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CouponIssueEvent {
    private Long couponId;
    private Long userId;
    private LocalDateTime requestedAt;
    private String requestId; // idempotency 보조(선택)
}
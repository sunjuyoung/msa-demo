package sun.board.coupon.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "coupon")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Coupon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private CouponType type; // 가입용 / 이벤트용

    // 쿠폰 코드 (이벤트, 가입 축하 등 구분)
    @Column(nullable = false, unique = true, length = 50)
    private String code;

    // 할인 금액 (정액 할인)
    @Column(nullable = false)
    private Integer discountAmount;

    // 최소 주문 금액
    @Column(nullable = false)
    private Integer minOrderAmount;

    // 유효기간
    @Column(nullable = false)
    private LocalDateTime expirationDate;

    // 총 발급 수량 (선착순 이벤트용)
    @Column(nullable = false)
    private Integer totalQuantity;

    // 남은 수량
    @Column(nullable = false)
    private Integer remainingQuantity;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    // ==== 비즈니스 메서드 ====
    public boolean isExpired() {
        return LocalDateTime.now().isAfter(expirationDate);
    }

    public void decreaseQuantity() {
        if (remainingQuantity <= 0) {
            throw new IllegalStateException("쿠폰이 모두 소진되었습니다.");
        }
        this.remainingQuantity -= 1;
    }
}
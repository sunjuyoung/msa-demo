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
    private CouponType type; // SIGNUP / EVENT

    // 쿠폰 코드 (이벤트, 가입 축하 등 구분)
    @Column(nullable = false, unique = true, length = 50)
    private String code;

    // 할인 금액
    @Column(nullable = false)
    private Integer discountAmount;

    // 최소 주문 금액
    @Column(nullable = false)
    private Integer minOrderAmount;

    @Column(nullable=false) private LocalDateTime startDate;
    @Column(nullable=false) private LocalDateTime endDate;

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


    // ==== 비즈니스 로직 ====
    public boolean isExpired() {
        LocalDateTime now = LocalDateTime.now();
        return now.isAfter(endDate);
    }

    public void decreaseQuantity() {
        if (remainingQuantity <= 0) {
            throw new IllegalStateException("쿠폰이 모두 소진되었습니다.");
        }
        this.remainingQuantity -= 1;
    }
}
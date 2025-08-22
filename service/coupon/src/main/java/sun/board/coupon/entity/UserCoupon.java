package sun.board.coupon.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "user_coupon",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_user_coupon", columnNames = {"user_id", "coupon_id"})
        }
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class UserCoupon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 어떤 회원이 소유했는지
    @Column(name = "user_id", nullable = false)
    private Long userId;

    // 어떤 쿠폰을 소유했는지
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coupon_id", nullable = false)
    private Coupon coupon;

    // 사용 여부
    @Column(nullable = false)
    private boolean used;

    // 사용한 시각
    private LocalDateTime usedAt;

    // 발급 일시
    @Column(nullable = false, updatable = false)
    private LocalDateTime issuedAt;

    // ==== 비즈니스 로직 ====
    public void use() {
        if (this.used) {
            throw new IllegalStateException("이미 사용한 쿠폰입니다.");
        }
        if (coupon.isExpired()) {
            throw new IllegalStateException("쿠폰이 만료되었습니다.");
        }
        this.used = true;
        this.usedAt = LocalDateTime.now();
    }
}
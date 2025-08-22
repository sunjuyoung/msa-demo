package sun.board.coupon.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CouponRedisService {
    private final StringRedisTemplate stringRedisTemplate;

    public long incrIssuedCount(Long couponId) {
        String key = issuedKey(couponId);
        // INCR 원자연산
        return stringRedisTemplate.opsForValue().increment(key);
    }

    public void decrIssuedCount(Long couponId) {
        String key = issuedKey(couponId);
        stringRedisTemplate.opsForValue().decrement(key);
    }

    public boolean addIssuedUser(Long couponId, Long userId) {
        // 중복 발급 방지용(선택)
        return Boolean.TRUE.equals(
                stringRedisTemplate.opsForSet().add(usersKey(couponId), String.valueOf(userId))
        );
    }

    public void removeIssuedUser(Long couponId, Long userId) {
        stringRedisTemplate.opsForSet().remove(usersKey(couponId), String.valueOf(userId));
    }

    private String issuedKey(Long couponId) { return "coupon:%d:issued".formatted(couponId); }
    private String usersKey(Long couponId) { return "coupon:%d:users".formatted(couponId); }
}
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


    private String issuedKey(Long couponId) { return "coupon:%d:issued".formatted(couponId); }
    private String usersKey(Long couponId) { return "coupon:%d:users".formatted(couponId); }
}
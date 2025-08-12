package sun.board.view.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;

@Repository
@RequiredArgsConstructor
public class ProductViewDistributedLockRepository {


    private final StringRedisTemplate redisTemplate;

    //view::product::{product_id}::user::{user_id}::lock
    private  static final String KEY_FORMAT = "view::product::%s::user::%s::lock";

    public boolean lock(Long productId, Long userId){
        // TTL 3분
        Duration duration = Duration.ofMinutes(3);
        return redisTemplate.opsForValue().setIfAbsent(generateKey(productId, userId), "", duration);
    }


    private String generateKey(Long productId, Long userId){
        return KEY_FORMAT.formatted(productId, userId);
    }
}

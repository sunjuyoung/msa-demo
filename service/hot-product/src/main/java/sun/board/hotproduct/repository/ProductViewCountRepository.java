package sun.board.hotproduct.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;
import sun.board.hotproduct.utils.TimeCalculatorUtils;

import java.time.Duration;

@Repository
@RequiredArgsConstructor
public class ProductViewCountRepository {

    private final StringRedisTemplate redisTemplate;

    private static final String KEY_FORMAT = "hot-product::product::%d::view_count";

    public void createOrUpdate(Long productOptionId, Long viewCount){
        redisTemplate.opsForValue().set(
                generateKey(productOptionId),
                String.valueOf(viewCount),
                TimeCalculatorUtils.calculateDurationToMidnight()
        );
    }

    public Long read(Long productOptionId){
        String result = redisTemplate.opsForValue().get(generateKey(productOptionId));
        return result == null ? 0 : Long.valueOf(result);
    }


    private String generateKey(Long productOptionId){
        return KEY_FORMAT.formatted(productOptionId);
    }
}

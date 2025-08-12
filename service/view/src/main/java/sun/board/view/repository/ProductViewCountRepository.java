package sun.board.view.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ProductViewCountRepository{

    private final StringRedisTemplate redisTemplate;
    //view::product::{product_id}::view_count
    private  static final String KEY_FORMAT = "view::product::%d::view_count";


public Long read(Long productId){
    String result = redisTemplate.opsForValue().get(generateKey(productId));
    return result == null ? 0 : Long.valueOf(result);
}


public Long increase(Long productId){
    return  redisTemplate.opsForValue().increment(generateKey(productId));
}

private String generateKey(Long productId){
    return KEY_FORMAT.formatted(productId);
}

}

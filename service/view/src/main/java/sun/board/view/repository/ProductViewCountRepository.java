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


public Long read(Long productOptionId){
    String result = redisTemplate.opsForValue().get(generateKey(productOptionId));
    return result == null ? 0 : Long.valueOf(result);
}


public Long increase(Long productOptionId){
    return  redisTemplate.opsForValue().increment(generateKey(productOptionId));
}

private String generateKey(Long productOptionId){
    return KEY_FORMAT.formatted(productOptionId);
}

//0 으로 초기화
public void init(Long productOptionId) {
    redisTemplate.opsForValue().set(generateKey(productOptionId), "0");
}



}

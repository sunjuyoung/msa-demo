package sun.board.view.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;

@Repository
@RequiredArgsConstructor
public class ArticleViewDistributedLockRepository {

    private final StringRedisTemplate redisTemplate;

    //view::article::{article_id}::user::{user_id}::lock
    private  static final String KEY_FORMAT = "view::article::%s::user::%s::lock";

    public boolean lock(Long articleId, Long userId){
        // TTL 3분
        Duration duration = Duration.ofMinutes(3);
        return redisTemplate.opsForValue().setIfAbsent(generateKey(articleId, userId), "", duration);
    }


    private String generateKey(Long articleId, Long userId){
        return KEY_FORMAT.formatted(articleId, userId);
    }
}

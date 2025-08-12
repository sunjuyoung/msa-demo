package sun.board.hotarticle.repository;


import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Repository
@RequiredArgsConstructor
public class ArticleCreatedTimeRepository {


    //생성시간도 저장해놔서 오늘 게시글인지 게시글 서비스를 호출하지 않아도 알 수 있도록

    private final StringRedisTemplate redisTemplate;

    // hot-article::article::{articleId}::created-time
    private static final String KEY_FORMAT = "hot-article::article::%s::created-time";


    public void createOrUpdate(Long articleId, LocalDateTime createdAt, Duration ttl) {
        //createdAt을 문자열로 변환하여 저장
        redisTemplate.opsForValue().set(
                generateKey(articleId),
                String.valueOf(createdAt.toInstant(ZoneOffset.UTC).toEpochMilli()),
                ttl
        );
    }

    public void delete(Long articleId) {
        redisTemplate.delete(generateKey(articleId));
    }

    public LocalDateTime read(Long articleId) {
        String result = redisTemplate.opsForValue().get(generateKey(articleId));

        if (result == null) {
            return null;
        }

        return LocalDateTime.ofInstant(
                Instant.ofEpochMilli(Long.parseLong(result)),
                ZoneOffset.UTC
        );
    }

    private String generateKey(Long articleId) {
        return KEY_FORMAT.formatted(articleId);
    }
}

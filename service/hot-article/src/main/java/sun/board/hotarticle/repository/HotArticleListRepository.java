package sun.board.hotarticle.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.StringRedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
@Repository
@RequiredArgsConstructor
public class HotArticleListRepository {

    private final StringRedisTemplate redisTemplate;

    //hot-article::list::(yyyyMMdd)
    private static final String KEY_FORMAT = "hot-article::list::%s";

    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd");

    //상위 10개 인기글 저장
    public void add(Long articleId, LocalDateTime time, Long score, Long limit, Duration ttl){
        redisTemplate.executePipelined((RedisCallback<?>)  action -> { //한번의 연결로 여러개의 명령을 실행할 수 있게 해줌
            StringRedisConnection conn = (StringRedisConnection) action;
            String key = generateKey(time);
            conn.zAdd(key, score, String.valueOf(articleId)); //sorted set에 articleId를 score로 추가
            conn.zRemRange(key, 0, - limit - 1); //상위 limit개수 이상의 데이터는 삭제
            conn.expire(key, ttl.toSeconds()); //ttl만큼 저장
            return null;
        });

    }

    //게시글 삭제시 해당 게시글의 인기글 목록에서 삭제
    public void remove(Long articleId, LocalDateTime time){
        String key = generateKey(time);
        redisTemplate.opsForZSet().remove(key, String.valueOf(articleId));
    }

    private String generateKey(LocalDateTime time){
        return generateKey(TIME_FORMATTER.format(time));
    }

    private String generateKey(String dateStr){
        return KEY_FORMAT.formatted(dateStr);
    }


    public List<Long> readAll(String dateStr) {
        String key = generateKey(dateStr);
        return redisTemplate.opsForZSet().reverseRangeWithScores(key, 0, -1)
                .stream()
                .peek(tuple -> log.info("articleId={}, score={}", tuple.getValue(), tuple.getScore()))
                .map(ZSetOperations.TypedTuple::getValue)
                .map(Long::parseLong)
                .toList();
    }
}

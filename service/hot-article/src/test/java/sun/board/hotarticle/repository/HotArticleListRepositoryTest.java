package sun.board.hotarticle.repository;

import ch.qos.logback.core.util.TimeUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class HotArticleListRepositoryTest {

    @Autowired
    HotArticleListRepository hotArticleListRepository;

    @Test
    void addTest(){
        LocalDateTime time = LocalDateTime.of(2021, 1, 1, 0, 0, 0);
        long limit = 3;

        hotArticleListRepository.add(1L, time, 2L, limit, Duration.ofSeconds(20));
        hotArticleListRepository.add(2L, time, 3L, limit, Duration.ofSeconds(20));
        hotArticleListRepository.add(3L, time, 1L, limit, Duration.ofSeconds(20));
        hotArticleListRepository.add(4L, time, 5L, limit, Duration.ofSeconds(20));
        hotArticleListRepository.add(5L, time, 4L, limit, Duration.ofSeconds(20));
        List<Long> longs = hotArticleListRepository.readAll("20210101");
        assertEquals(3,longs.size());




    }

}
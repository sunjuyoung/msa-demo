package sun.board.view.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import sun.board.view.entity.ViewCount;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ViewCountBackUpRepositoryTest {

    @Autowired
    ViewCountBackUpRepository viewCountBackUpRepository;

    @PersistenceContext
    private EntityManager entityManager;


    @Test
    @Transactional
    void updateViewCOunt(){
       viewCountBackUpRepository
                .save(ViewCount.init(1L, 0L));

        entityManager.flush();
        entityManager.clear();

        int result = viewCountBackUpRepository.updateViewCount(1L, 10L);
        int i = viewCountBackUpRepository.updateViewCount(1L, 5L);

        assertEquals(1, result);
        assertEquals(0, i);


        viewCountBackUpRepository.findById(1L)
                .ifPresent(viewCount -> {
                    assertEquals(10L, viewCount.getViewCount());
                });
    }

}
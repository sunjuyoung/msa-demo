package sun.board.article.data;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import sun.board.common.snowflake.Snowflake;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.support.TransactionTemplate;
import sun.board.article.entity.Article;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SpringBootTest
public class DataInitializer {

    @PersistenceContext
    EntityManager entityManager;
    @Autowired
    TransactionTemplate transactionTemplate;
    Snowflake snowflake = new Snowflake();
    CountDownLatch latch = new CountDownLatch(EXECUTE_COUNT);


    static final int BULK_INSERT_SIZE = 2000;
    static final int EXECUTE_COUNT = 3000;

    @Test
    void initialize() throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        for(int i=0; i< EXECUTE_COUNT; i++){
            executorService.submit(()->{
                insert();
                latch.countDown();
                System.out.println("latch count =" +latch.getCount());
            });
        }
        latch.await();
        executorService.shutdown();

    }

    void insert(){
        transactionTemplate.executeWithoutResult(transactionStatus ->{
            for(int i=0; i<BULK_INSERT_SIZE; i++){

                entityManager.persist(Article.create(
                        snowflake.nextId(),
                        "title"+i,
                        "content"+i,
                        1L,
                        1L
                ));
            }
        });


    }
}

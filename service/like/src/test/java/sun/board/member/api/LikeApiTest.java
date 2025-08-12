package sun.board.member.api;

import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestClient;
import sun.board.member.service.response.ArticleLikeResponse;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LikeApiTest {

    RestClient restClient = RestClient.create("http://localhost:9002");

    @Test
    void liekAndUnlikeTest(){

//        Long articleId = 999L;
//
//        like(articleId, 1L);
//        like(articleId, 2L);
//        like(articleId, 3L);
//
//
//        ArticleLikeResponse read1 = read(articleId, 1L);
//        ArticleLikeResponse read = read(articleId, 2L);
//        ArticleLikeResponse read2 = read(articleId, 3L);
//
//        System.out.println(read1);
//        System.out.println(read);
//        System.out.println(read2);
//
//        unlike(articleId, 1L);
//        unlike(articleId, 2L);
//        unlike(articleId, 3L);





    }


    void like(Long articleId, Long userId, String lockType){
        restClient.post()
                .uri("/v1/article-likes/article/{articleId}/user/{userId}/"+ lockType , articleId, userId)
                .retrieve();
    }

    void unlike(Long articleId, Long userId, String lockType){
        restClient.delete()
                .uri("/v1/article-likes/article/{articleId}/user/{userId}/"+ lockType, articleId, userId)
                .retrieve();
    }

    ArticleLikeResponse read(Long articleId, Long userId){
        return restClient.get()
                .uri("/v1/article-likes/article/{articleId}/user/{userId}", articleId, userId)
                .retrieve()
                .body(ArticleLikeResponse.class);

    }



    @Test
    void likePerformanceTest() throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(100);
        likePerformanceTest(executorService, 1111L, "pessimistic-lock");
        likePerformanceTest(executorService, 2222L, "pessimistic-lock2");
        likePerformanceTest(executorService, 3333L, "optimistic-lock");
    }

    void likePerformanceTest(ExecutorService executorService, Long articleId, String lockType) throws InterruptedException {

        CountDownLatch latch = new CountDownLatch(3000);
        System.out.println(lockType + " start");

        like(articleId, 1L, lockType);

        long start = System.nanoTime();
        for(int i=0; i < 3000; i++) {
            long userId = i + 2;
            executorService.submit(() -> {
                like(articleId, userId, lockType);
                latch.countDown();
            });
        }

        latch.await();

        long end = System.nanoTime();

        System.out.println("lockType = " + lockType + ", time = " + (end - start) / 1000000 + "ms");
        System.out.println(lockType + " end");

        Long count = restClient.get()
                .uri("/v1/article-likes/article/{articleId}/count", articleId)
                .retrieve()
                .body(Long.class);

        System.out.println("count = " + count);
    }
}

package sun.board.product.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.TestPropertySource;
import sun.board.product.dto.ProductUpdateStockDto;
import sun.board.product.exception.ex.LockAcquisitionFailedException;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
@SpringBootTest
class RedissonLockStockTest {
    @Autowired
    private RedissonLockStock redissonLockStock;



    @Test
    @DisplayName("동시에 여러 스레드가 재고 감소를 시도할 때 순차적으로 처리된다")
    void decreaseStock_ConcurrentAccess() throws InterruptedException {
        // Given
        ProductUpdateStockDto dto = ProductUpdateStockDto.builder()
                .productId(1L)
                .stockQuantity(1)
                .build();

        AtomicInteger successCount = new AtomicInteger(0);
        AtomicInteger failCount = new AtomicInteger(0);
        int threadCount = 10;
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);



        // When
        for (int i = 0; i < threadCount; i++) {
            executorService.submit(() -> {
                try {
                    redissonLockStock.decreaseStock(dto);
                    successCount.incrementAndGet();
                } catch (LockAcquisitionFailedException e) {
                    failCount.incrementAndGet();
                }catch (Exception e) {
                    failCount.incrementAndGet();
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await(30, TimeUnit.SECONDS);
        executorService.shutdown();

        // Then
        assertThat(successCount.get()).isGreaterThan(0);
        assertThat(successCount.get() + failCount.get()).isEqualTo(threadCount);

        // 성공한 요청 수만큼 productService 호출되어야 함
    }
}
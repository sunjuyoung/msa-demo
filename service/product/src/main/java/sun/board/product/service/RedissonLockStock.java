package sun.board.product.service;

import lombok.RequiredArgsConstructor;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;
import sun.board.product.dto.ProductUpdateStockDto;
import sun.board.product.exception.ex.LockAcquisitionFailedException;

import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class RedissonLockStock {

    private final RedissonClient redissonClient;
    private final ProductService productService;

    public void decreaseStock(ProductUpdateStockDto dto) {
        Long productId = dto.getProductId();
        String lockKey = "product-lock:" + productId;
        var lock = redissonClient.getLock(lockKey);
        try {
            if (lock.tryLock(5, 10, TimeUnit.SECONDS)) {
                productService.productMinusStock(dto);
            } else {
                throw new LockAcquisitionFailedException("재고 lock 획득 실패");
            }
        }catch (InterruptedException e){
            Thread.currentThread().interrupt(); // 스레드 인터럽트 상태 복구
            throw new LockAcquisitionFailedException("락 획득 중 스레드 인터럽트 발생");

        } finally {
            if (lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }

    public void increaseStock(Long productId, int quantity) {
        String lockKey = "product-lock:" + productId;
        var lock = redissonClient.getLock(lockKey);
        try {
            if (lock.tryLock()) {
                ProductUpdateStockDto dto = new ProductUpdateStockDto(productId, quantity);
                productService.productPlusStock(dto);
            } else {
                throw new IllegalStateException("재고 잠금 실패");
            }
        } finally {
            if (lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }

}

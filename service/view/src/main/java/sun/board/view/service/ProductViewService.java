package sun.board.view.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import sun.board.view.repository.ProductViewCountRepository;
import sun.board.view.repository.ProductViewDistributedLockRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductViewService {

    private final ProductViewDistributedLockRepository productViewDistributedLockRepository;
    private final ProductViewCountRepository productViewCountRepository;
    private final ProductViewCountBackupProcessor productViewCountBackupProcessor;

    private static final int BACKUP_COUNT = 10;

    public Long increase(Long productOptionId, Long userId) {

        if(!productViewDistributedLockRepository.lock(productOptionId, userId)) {
            return productViewCountRepository.read(productOptionId);
        }

        Long count = productViewCountRepository.increase(productOptionId);

        if(count % BACKUP_COUNT == 0) {
            productViewCountBackupProcessor.backup(productOptionId, count);
            productViewCountRepository.init(productOptionId);
        }

        return count;
    }

    public Long count(Long productOptionId) {
        return productViewCountRepository.read(productOptionId);
    }
}

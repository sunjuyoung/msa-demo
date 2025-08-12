package sun.board.view.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sun.board.view.repository.ArticleViewCountRepository;
import sun.board.view.repository.ArticleViewDistributedLockRepository;

@Service
@RequiredArgsConstructor
public class ArticleViewService {
    private final ArticleViewCountBackupProcessor articleViewCountBackupProcessor;
    private final ArticleViewCountRepository articleViewCountRepository;

    private final ArticleViewDistributedLockRepository articleViewDistributedLockRepository;

    private static final int BAKCUP_COUNT = 10;

    public Long increase(Long articleId, Long userId){

        // 분산락을 사용하여 중복 증가 어뷰징 방지
        if(!articleViewDistributedLockRepository.lock(articleId, userId)){
            return articleViewCountRepository.read(articleId);
        }

        Long count = articleViewCountRepository.increase(articleId);
        
        // 100회마다 백업
        if(count % BAKCUP_COUNT == 0){
            articleViewCountBackupProcessor.backup(articleId, count);
        }
        return count;
    }

    public Long count(Long articleId){
        return articleViewCountRepository.read(articleId);
    }
}

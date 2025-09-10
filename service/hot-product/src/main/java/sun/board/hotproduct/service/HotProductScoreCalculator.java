package sun.board.hotproduct.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import sun.board.hotproduct.repository.ProductLikeCountRepository;
import sun.board.hotproduct.repository.ProductViewCountRepository;

@Component
@RequiredArgsConstructor
public class HotProductScoreCalculator {

    private final ProductLikeCountRepository productLikeCountRepository;
    private final ProductViewCountRepository productViewCountRepository;

    //가중치
    private static final long PRODUCT_VIEW_COUNT_WEIGHT = 1;
    private static final long PRODUCT_LIKE_COUNT_WEIGHT = 3;
    private static final long PRODUCT_ORDER_COUNT_WEIGHT = 5;


    public long calculate(Long productOptionId){

        long viewCount = productViewCountRepository.read(productOptionId);
        long likeCount = productLikeCountRepository.read(productOptionId);

        return (viewCount * PRODUCT_VIEW_COUNT_WEIGHT)
                + (likeCount * PRODUCT_LIKE_COUNT_WEIGHT);
    }
}

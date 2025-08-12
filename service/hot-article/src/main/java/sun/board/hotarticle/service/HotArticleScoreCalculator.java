package sun.board.hotarticle.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import sun.board.hotarticle.repository.ArticleCommentCountRepository;
import sun.board.hotarticle.repository.ArticleLikeCountRepository;
import sun.board.hotarticle.repository.ArticleViewCountRepository;

@Component
@RequiredArgsConstructor
public class HotArticleScoreCalculator {

    private final ArticleViewCountRepository articleViewCountRepository;
    private final ArticleLikeCountRepository articleLikeCountRepository;
    private final ArticleCommentCountRepository articleCommentCountRepository;

    //가중치
    private static final long ARTICLE_LIKE_COUNT_WEIGHT = 3;
    private static final long ARTICLE_COMMENT_COUNT_WEIGHT = 2;
    private static final long ARTICLE_VIEW_COUNT_WEIGHT = 1;

    public long calculate(Long articleId) {
        Long likeCount = articleLikeCountRepository.read(articleId);
        Long viewCount = articleViewCountRepository.read(articleId);
        Long commentCount = articleCommentCountRepository.read(articleId);

        return likeCount * ARTICLE_LIKE_COUNT_WEIGHT
                + viewCount * ARTICLE_VIEW_COUNT_WEIGHT
                + commentCount * ARTICLE_COMMENT_COUNT_WEIGHT;
    }
}

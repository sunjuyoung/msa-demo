package sun.board.hotarticle.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import sun.board.hotarticle.repository.ArticleCommentCountRepository;
import sun.board.hotarticle.repository.ArticleLikeCountRepository;
import sun.board.hotarticle.repository.ArticleViewCountRepository;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class HotArticleScoreCalculatorTest {

    @InjectMocks
    private HotArticleScoreCalculator hotArticleScoreCalculator;
    @Mock
    ArticleLikeCountRepository articleLikeCountRepository;
    @Mock
    ArticleViewCountRepository articleViewCountRepository;
    @Mock
    ArticleCommentCountRepository articleCommentCountRepository;

    @Test
    void calculate() {
        // given
        Long articleId = 1L;
        Long likeCount = 10L; //30
        Long viewCount = 20L; //20
        Long commentCount = 30L; //60

        // when
        when(articleLikeCountRepository.read(articleId)).thenReturn(likeCount);
        when(articleViewCountRepository.read(articleId)).thenReturn(viewCount);
        when(articleCommentCountRepository.read(articleId)).thenReturn(commentCount);

        // then
        assertEquals(110, hotArticleScoreCalculator.calculate(articleId));
    }

}
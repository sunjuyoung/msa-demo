package sun.board.article.repository;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import sun.board.article.entity.Article;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
class ArticleRepositoryTest {

    @Autowired
    ArticleRepository articleRepository;

    @Test
    void findallTest(){
        List<Article> articles = articleRepository.findAll(1L, 120000L, 30L);
        log.info("articles.size()={}",articles.size());
        for (Article article : articles) {
            log.info("article={}",article);
        }
    }

    @Test
    void countTest(){
        Long count = articleRepository.count(1L, 3000L);
        log.info("count={}",count);
    }

    @Test
    void findAllInfiniteScrollTest(){
        List<Article> articles = articleRepository.findAllInfiniteScroll(1L, 30L);
        log.info("articles.size()={}",articles.size());
        for (Article article : articles) {
            log.info("article={}",article.getArticleId());
        }

        Long lastArticleId = articles.getLast().getArticleId();
        List<Article> nextArticles = articleRepository.findAllInfiniteScroll(1L, 30L, lastArticleId);
        log.info("nextArticles.size()={}",nextArticles.size());
        for (Article article : nextArticles) {
            log.info("article={}",article.getArticleId());
        }
    }


}
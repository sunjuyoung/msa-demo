package sun.board.hotarticle.service.response;

import lombok.Getter;
import sun.board.hotarticle.client.ArticleClient;

import java.time.LocalDateTime;

@Getter
public class HotArticleResponse {

    private Long articleId;
    private String title;
    private LocalDateTime createdAt;

    public static HotArticleResponse from(ArticleClient.ArticleResponse articleResponse) {
        HotArticleResponse hotArticleResponse = new HotArticleResponse();
        hotArticleResponse.articleId = articleResponse.getArticleId();
        hotArticleResponse.title = articleResponse.getTitle();
        hotArticleResponse.createdAt = articleResponse.getCreatedAt();
        return hotArticleResponse;
    }
}

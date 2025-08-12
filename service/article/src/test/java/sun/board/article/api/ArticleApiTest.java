package sun.board.article.api;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestClient;
import sun.board.article.service.request.ArticleCreatRequest;
import sun.board.article.service.response.ArticleResponse;

public class ArticleApiTest {

    RestClient restClient = RestClient.create("http://localhost:9000");


    @Test
    void createTest(){

        ArticleResponse response = create(new ArticleCreatRequest("test", "test", 1L, 1L));
        System.out.println(response);


    }

    ArticleResponse create(ArticleCreatRequest request){
        return restClient.post()
                .uri("/v1/articles")
                .body(request)
                .retrieve()
                .body(ArticleResponse.class);
    }

    @Getter
    @AllArgsConstructor
    static class ArticleCreatRequest{
        private String title;
        private String content;
        private Long writerId;
        private Long boardId;

    }

}

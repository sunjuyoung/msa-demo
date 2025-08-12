package sun.board.articleread.client;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class ArticleClient {

    private final RestClient.Builder restClientBuilder;

    private RestClient restClient;

    @PostConstruct
    void initRestClient() {
        // Eureka 서비스명 기반 URI
        restClient = restClientBuilder
                .baseUrl("http://article-service") // Eureka에 등록된 서비스 이름
                .build();
    }



    public Optional<ArticleResponse> read(Long articleId){
        try {
            return Optional.ofNullable(restClient.get()
                    .uri("/articles/{articleId}", articleId)
                    .retrieve()
                    .body(ArticleResponse.class));
        }catch (Exception e){
            log.error("Article read articleId={}", articleId,e);
            return Optional.empty();
        }


    }

    @Getter
    public static class ArticleResponse{
        private Long articleId;
        private String title;
        private String content;
        private Long boardId;
        private Long writerId;
        private LocalDateTime updatedAt;
        private LocalDateTime createdAt;
    }
}

package sun.board.articleread.client;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Slf4j
@Component
@RequiredArgsConstructor
public class LikeClient {

    private final RestClient.Builder restClientBuilder;

    private RestClient restClient;

    @PostConstruct
    void initRestClient() {
        // Eureka 서비스명 기반 URI
        restClient = restClientBuilder
                .baseUrl("http://like-service") // Eureka에 등록된 서비스 이름
                .build();
    }



    public long count(Long articleId){
        try {
            return restClient.get()
                    .uri("/likes/article/{articleId}/count", articleId)
                    .retrieve()
                    .body(Long.class);
        }catch (Exception e){
            log.error("article-likes count  articleId={}", articleId,e);
        }

        return 0;
    }


}

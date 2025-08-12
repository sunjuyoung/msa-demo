package sun.board.articleread.client;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Slf4j
@Component
@RequiredArgsConstructor
public class ViewClient {

    private final RestClient.Builder restClientBuilder;

    private RestClient restClient;

    @PostConstruct
    void initRestClient() {
        // Eureka 서비스명 기반 URI
        restClient = restClientBuilder
                .baseUrl("http://view-service") // Eureka에 등록된 서비스 이름
                .build();
    }



    //레디스에서 데이터를 조회
    //레디스에 데이터가 없었다면 count 메소드 내부 로직이 호출되면서 viewservice로 원본데이터를 요청
    //원본데이터를 받아서 레디스에 저장하고 리턴
    @Cacheable(key = "#articleId", value = "articleViewCount")
    public long count(Long articleId){
        log.info("ViewClient count articleId={}", articleId);
        try {
            return restClient.get()
                    .uri("/views/articles/{articleId}/count", articleId)
                    .retrieve()
                    .body(Long.class);
        }catch (Exception e){
            log.error("comment count  articleId={}", articleId,e);
        }

        return 0;
    }


}

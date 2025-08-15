package sun.board.ordering.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class RestTemplateConfig {

    @Bean
    @LoadBalanced // 서비스명 기반 호출 가능 (ex: http://product-service)
    public WebClient productWebClient() {
        return WebClient.builder()
                .baseUrl("http://localhost:8080/product-service") // product-service의 기본 URL 설정
                .build();
    }

}
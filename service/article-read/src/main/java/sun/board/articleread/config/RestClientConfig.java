package sun.board.articleread.config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {

    @Bean
    @LoadBalanced // Eureka 서비스명으로 해석 가능하게 함
    public RestClient.Builder restClientBuilder() {
        return RestClient.builder();
    }
}
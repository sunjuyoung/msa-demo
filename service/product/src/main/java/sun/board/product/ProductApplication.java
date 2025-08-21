package sun.board.product;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@EnableDiscoveryClient
@SpringBootApplication
public class ProductApplication {

    public static void main(String[] args){
        SpringApplication.run(ProductApplication.class, args);
    }

    CommandLineRunner commandLineRunner() {
        return args -> {
            // 초기화 작업을 여기에 추가할 수 있습니다.
            System.out.println("Product Application has started successfully.");
        };
    }
}

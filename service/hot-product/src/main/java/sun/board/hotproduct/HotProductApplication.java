package sun.board.hotproduct;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;


@EnableDiscoveryClient
@SpringBootApplication
public class HotProductApplication {
    public static void main(String[] args) {
        SpringApplication.run(HotProductApplication.class,args);
    }
}

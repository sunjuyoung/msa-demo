package sun.board;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;


@EnableFeignClients
@EnableJpaAuditing
@EnableDiscoveryClient
@SpringBootApplication
public class OrderPaymentApplication {

    public static void main(String[] args) {
        SpringApplication.run(OrderPaymentApplication.class, args);
    }
}

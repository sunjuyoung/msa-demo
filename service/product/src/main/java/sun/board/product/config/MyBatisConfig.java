package sun.board.product.config;

import org.apache.ibatis.type.JdbcType;
import org.mybatis.spring.boot.autoconfigure.ConfigurationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import sun.board.product.service.handler.IntegerListTypeHandler;

import java.util.List;

@Configuration
public class MyBatisConfig {

    @Bean
    public ConfigurationCustomizer mybatisConfigurationCustomizer() {
        return configuration -> {
            configuration.getTypeHandlerRegistry()
                    .register(List.class, JdbcType.VARCHAR, IntegerListTypeHandler.class);
        };
    }
}
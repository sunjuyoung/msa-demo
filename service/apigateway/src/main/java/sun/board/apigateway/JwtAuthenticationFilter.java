package sun.board.apigateway;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.regex.Pattern;

@Slf4j
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)

public class JwtAuthenticationFilter implements GlobalFilter {

    @Value("${jwt.secretKey}")
    private String jwtSecret;

    private static final List<Pattern> WHITELIST_PATHS = List.of(
            Pattern.compile("^/member/create$"),
            Pattern.compile("^/member/doLogin$"),
            Pattern.compile("^/member/refresh-token$"),
            Pattern.compile("^/articles/.*$"),
            Pattern.compile("^/product/.*$"),
            Pattern.compile("^/article-views/.*$"),
            //hot-articles/
            Pattern.compile("^/hot-articles/.*$"),
            // /article-read-service/* 에서 제공하는 API는 모두 허용
            Pattern.compile("^/article-read/.*$"),
            Pattern.compile("^/payments/.*$")




    );

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String rawPath = exchange.getRequest().getURI().getRawPath();
// 예: "/member-service/member/doLogin"

        String strippedPath = rawPath.replaceFirst("^/[^/]+", "");
// 서비스명(prefix) 제거 → "/member/doLogin"
        log.info(strippedPath);

        if (isWhitelisted(strippedPath)) {
            return chain.filter(exchange);
        }

        String token = extractToken(exchange.getRequest());

        if (token == null || !isValid(token)) {
            log.warn("Invalid or missing JWT token");
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        Claims claims = getClaims(token);

        // 사용자 정보를 downstream 서비스에 전달
        ServerHttpRequest mutatedRequest = exchange.getRequest().mutate()
                .header("X-User-Id", claims.getSubject())
                .header("X-User-Role", claims.get("role", String.class))
                .build();

        return chain.filter(exchange.mutate().request(mutatedRequest).build());
    }

    private boolean isWhitelisted(String path) {
        return WHITELIST_PATHS.stream()
                .anyMatch(pattern -> pattern.matcher(path).matches());
    }

    private String extractToken(ServerHttpRequest request) {
        List<String> authHeaders = request.getHeaders().getOrEmpty("Authorization");
        if (authHeaders.isEmpty()) return null;

        String bearer = authHeaders.get(0);
        if (!bearer.startsWith("Bearer ")) return null;

        return bearer.substring(7);
    }

    private boolean isValid(String token) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            log.error("JWT validation error: {}", e.getMessage());
            return false;
        }
    }

    private Claims getClaims(String token) {
        return Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token)
                .getBody();
    }
}

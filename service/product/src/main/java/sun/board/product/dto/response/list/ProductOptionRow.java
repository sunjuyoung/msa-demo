package sun.board.product.dto.response.list;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ProductOptionRow {
    private Long productId;
    private String color;  // enum -> uppercase 문자열(DB 기준), 서비스에서 소문자 변환 가능
    private Integer size;
}
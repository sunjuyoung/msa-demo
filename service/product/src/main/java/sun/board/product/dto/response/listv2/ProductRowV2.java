package sun.board.product.dto.response.listv2;

import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductRowV2 {
    private Long productId;
    private String name;
    private String description;
    private Integer price;
    private String category;
    private LocalDateTime createdAt;

    // 한방쿼리에서 CROSS JOIN totals 로 들어오는 값
    private Integer totalCount;

    @Builder.Default
    private Map<Long, Long> likeCounts = new HashMap<>();

    // resultMap <collection> 으로 수집됨
    @Builder.Default
    private List<ProductOptionRowV2> options = new ArrayList<>();
}
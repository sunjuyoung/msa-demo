package sun.board.product.dto.response.list;

import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductListItemResponse {
    private Long productId;
    private String name;
    private String description;
    private BigDecimal price;
    private String category;
    private List<Integer> size;   // 예: [90, 95, 100]
    private List<String> color;   // 예: ["black", "white", "beige"]
}
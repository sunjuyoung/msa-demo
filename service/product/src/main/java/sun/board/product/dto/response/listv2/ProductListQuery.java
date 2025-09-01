package sun.board.product.dto.response.listv2;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductListQuery {
    // 필터
    private String name;
    private String category;
    private Integer minPrice;
    private Integer maxPrice;
    private List<String> colors;
    private List<String> sizes;

    // 페이징
    private int size;   // LIMIT
    private int offset; // OFFSET
}
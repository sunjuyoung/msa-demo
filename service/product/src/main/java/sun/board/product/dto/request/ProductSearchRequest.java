package sun.board.product.dto.request;


import lombok.*;
import sun.board.product.entity.enums.ProductCategory;
import sun.board.product.entity.enums.ProductColor;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductSearchRequest {
    private String name;
    private ProductCategory category;
    private BigDecimal minPrice;
    private BigDecimal maxPrice;
    private List<ProductColor> colors;
    private List<Integer> sizes;
    private Boolean inStock;

    @Builder.Default
    private int page = 0;

    @Builder.Default
    private int size = 20;

    @Builder.Default
    private String sortBy = "createdAt";

    @Builder.Default
    private String sortDirection = "DESC";

    // offset 필드 추가
    private int offset;
}

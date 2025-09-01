package sun.board.product.dto.request;

import jakarta.validation.constraints.Min;
import lombok.*;
import org.springframework.util.CollectionUtils;
import sun.board.product.dto.response.listv2.ProductListQuery;

import java.util.List;

@Getter@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductListOneShotRequest {

    private String name;
    private String category;

    @Min(0) private Integer minPrice;
    @Min(0) private Integer maxPrice;

    private List<String> colors;
    private List<String> sizes;

    @Min(1) private Integer size = 10;   // page size
    @Min(0) private Integer page = 0;    // 0-base

    public ProductListQuery toQuery() {
        // 색상/사이즈 대문자화(옵션 컬럼이 UPPER 저장/인덱스 기준일 때 권장)
        List<String> normColors = CollectionUtils.isEmpty(colors) ? null :
                colors.stream().map(s -> s == null ? null : s.toUpperCase()).toList();
        List<String> normSizes = CollectionUtils.isEmpty(sizes) ? null :
                sizes.stream().map(s -> s == null ? null : s.toUpperCase()).toList();

        int limit = size != null ? size : 20;
        int offset = (page != null ? page : 0) * limit;

        return ProductListQuery.builder()
                .name(name)
                .category(category)
                .minPrice(minPrice)
                .maxPrice(maxPrice)
                .colors(normColors)
                .sizes(normSizes)
                .size(limit)
                .offset(offset)
                .build();
    }
}

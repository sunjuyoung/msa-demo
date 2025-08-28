package sun.board.product.mapper;


import org.apache.ibatis.annotations.Mapper;
import org.springframework.data.repository.query.Param;
import sun.board.product.dto.request.ProductSearchRequest;
import sun.board.product.dto.response.list.ProductOptionRow;
import sun.board.product.dto.response.list.ProductRow;

import java.util.List;

@Mapper
public interface ProductMapper {

    List<ProductOptionRow> findProductsWithOptions(ProductSearchRequest searchRequest);

    int countProducts(ProductSearchRequest searchRequest);


    // 2-1. 페이지 대상이 되는 상품 id만 먼저 가져옴 (상품 단위 페이징 핵심)
    List<Long> findProductPageIds(
            @Param("offset") int offset,
            @Param("size") int size,
            @Param("name") String name,
            @Param("category") String category,
            @Param("minPrice") Integer minPrice,
            @Param("maxPrice") Integer maxPrice,
            @Param("sizes") List<Integer> sizes,
            @Param("colors") List<String> colors
    );

    // 2-2. 총 상품 수 (페이지 계산용)
    int countProducts(
            @Param("name") String name,
            @Param("category") String category,
            @Param("minPrice") Integer minPrice,
            @Param("maxPrice") Integer maxPrice,
            @Param("sizes") List<Integer> sizes,
            @Param("colors") List<String> colors
    );

    // 2-3. 위에서 뽑은 id 목록으로 상품 기본정보 일괄 조회
    List<ProductRow> findProductsByIds(
            @Param("ids") List<Long> productIds
    );

    // 2-4. 위에서 뽑은 id 목록으로 옵션 일괄 조회 (재고 제외, 상태만 필터)
    List<ProductOptionRow> findOptionsByProductIds(
            @Param("ids") List<Long> ids
    );
}

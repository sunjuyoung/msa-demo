package sun.board.product.mapper;


import org.apache.ibatis.annotations.Mapper;
import sun.board.product.dto.request.ProductSearchRequest;
import sun.board.product.dto.response.ProductDetailResponse;

import java.util.List;

@Mapper
public interface ProductMapper {

    List<ProductDetailResponse> findProductsWithOptions(ProductSearchRequest searchRequest);

    int countProducts(ProductSearchRequest searchRequest);
}

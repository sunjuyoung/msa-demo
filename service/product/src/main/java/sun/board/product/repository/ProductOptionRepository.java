package sun.board.product.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import sun.board.product.entity.ProductOption;
import sun.board.product.entity.enums.OptionStatus;
import sun.board.product.entity.enums.ProductColor;
import sun.board.product.grpc.response.ProductGrpcResponse;

import java.util.List;

public interface ProductOptionRepository extends JpaRepository<ProductOption,Long> {
    List<ProductOption> findByProductIdIn(List<Long> productIds);

    List<ProductOption> findByProductIdAndStatusAndStockGreaterThan(Long productId, OptionStatus status, int stock);



    @Query("SELECT po FROM ProductOption po WHERE po.product.id = :productId AND po.color = :color AND po.size = :size")
    ProductOption findByProductIdAndColorAndSize(Long productId, ProductColor color, int size);


    @Query(value ="" +
            "select p.name, p.price,p.description,p.category, po.id,po.color, po.size,po.stock " +
            " from product_option po " +
            " inner join product p on po.product_id = p.id " +
            " where po.id = :productOptionId"
    ,nativeQuery = true
    )
    ProductGrpcResponse findProductOptionWithProduct(@Param("productOptionId") Long productOptionId);

}

package sun.board.product.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sun.board.product.entity.ProductOption;
import sun.board.product.entity.enums.OptionStatus;

import java.util.List;

public interface ProductOptionRepository extends JpaRepository<ProductOption,Long> {
    List<ProductOption> findByProductIdIn(List<Long> productIds);

    List<ProductOption> findByProductIdAndStatusAndStockGreaterThan(Long productId, OptionStatus status, int stock);



}

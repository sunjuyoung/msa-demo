package sun.board.product.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sun.board.product.entity.Product;

public interface ProductRepository extends JpaRepository<Product,Long> {
}

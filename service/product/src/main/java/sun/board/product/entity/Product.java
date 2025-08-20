package sun.board.product.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import sun.board.product.entity.enums.ProductCategory;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Table(
        name = "product",
        indexes = {
                @Index(name = "idx_name", columnList = "name"),
                @Index(name = "idx_category_price", columnList = "category, price"),
                @Index(name = "idx_category_name", columnList = "category, name")
        }
)
public class Product extends BaseTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "price", nullable = false)
    private BigDecimal price;

    @Column(length = 1000)
    private String description;


    @Column(name = "stock_quantity", nullable = false)
    private Integer stockQuantity;


    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProductCategory category;


    // 상품 옵션들과의 연관관계
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL,
            orphanRemoval = true, fetch = FetchType.LAZY)
    private List<ProductOption> productOptions = new ArrayList<>();




    public void minusStockQuantity(int stockQuantity) {
        if (stockQuantity < 0) {
            throw new IllegalArgumentException("감소할 재고는 0보다 작을 수 없습니다.");
        }
        this.stockQuantity = this.stockQuantity - stockQuantity;
    }

    public void plusStockQuantity(int stockQuantity) {
        if (stockQuantity < 0) {
            throw new IllegalArgumentException("증가할 재고는 0보다 작을 수 없습니다.");
        }
        this.stockQuantity = this.stockQuantity + stockQuantity;
    }

}
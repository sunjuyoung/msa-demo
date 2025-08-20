package sun.board.product.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import sun.board.product.entity.enums.OptionStatus;
import sun.board.product.entity.enums.ProductColor;

import java.math.BigDecimal;

// 상품 옵션 (색상 + 사이즈 조합)
@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "product_option",
        indexes = {
                @Index(name = "idx_product_option_product", columnList = "product_id"),

        },
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_product_color_size",
                        columnNames = {"product_id", "color", "size"})
        })
public class ProductOption {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProductColor color;

    @Column(nullable = false)
    private Integer size; // 90~110

    @Column(nullable = false)
    private Integer stock; // 재고


    @Enumerated(EnumType.STRING)
    private OptionStatus status = OptionStatus.AVAILABLE; // AVAILABLE, OUT_OF_STOCK, DISCONTINUED



    // 생성 메서드
    public static ProductOption create(Product product,
                                       ProductColor color,
                                       Integer size,
                                       Integer stock) {
        ProductOption option = ProductOption.builder()
                .product(product)
                .color(color)
                .size(size)
                .stock(stock)
                .status(OptionStatus.AVAILABLE)
                .build();

        product.getProductOptions().add(option); // 양방향 동기화
        return option;
    }


    // 비즈니스 로직
    public boolean isAvailable() {
        return status == OptionStatus.AVAILABLE && stock > 0;
    }

    public void decreaseStock(int quantity) {
        if (stock < quantity) {
            throw new IllegalArgumentException("재고가 부족합니다.");
        }
        this.stock -= quantity;
        if (this.stock == 0) {
            this.status = OptionStatus.OUT_OF_STOCK;
        }
    }

    public void increaseStock(int quantity) {
        this.stock += quantity;
        if (this.status == OptionStatus.OUT_OF_STOCK && this.stock > 0) {
            this.status = OptionStatus.AVAILABLE;
        }
    }


}
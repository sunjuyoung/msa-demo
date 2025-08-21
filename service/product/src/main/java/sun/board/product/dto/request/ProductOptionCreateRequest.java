package sun.board.product.dto.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import sun.board.product.entity.Product;
import sun.board.product.entity.ProductOption;
import sun.board.product.entity.enums.OptionStatus;
import sun.board.product.entity.enums.ProductColor;

import java.math.BigDecimal;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductOptionCreateRequest {

    @NotNull(message = "색상은 필수입니다.")
    private ProductColor color;

    @NotNull(message = "사이즈는 필수입니다.")
    @Min(value = 90, message = "사이즈는 90 이상이어야 합니다.")
    @Max(value = 110, message = "사이즈는 110 이하여야 합니다.")
    private Integer size;

    @NotNull(message = "재고는 필수입니다.")
    @Min(value = 0, message = "재고는 0 이상이어야 합니다.")
    @Max(value = 9999, message = "재고는 9999 이하로 입력해주세요.")
    private Integer stock;


    // 옵션 엔티티로 변환
    public ProductOption toEntity(Product product) {
        return ProductOption.builder()
                .product(product)
                .color(color)
                .size(size)
                .stock(stock)
                .status( OptionStatus.AVAILABLE )
                .build();
    }
}
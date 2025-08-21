package sun.board.product.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import sun.board.product.entity.Product;
import sun.board.product.entity.enums.ProductCategory;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductCreateRequest {

    @NotBlank(message = "상품명은 필수입니다.")
    @Size(max = 200, message = "상품명은 200자 이내로 입력해주세요.")
    private String name;

    @Size(max = 1000, message = "상품 설명은 1000자 이내로 입력해주세요.")
    private String description;

    @NotNull(message = "카테고리는 필수입니다.")
    private ProductCategory category;

    @NotNull(message = "기본 가격은 필수입니다.")
    @DecimalMin(value = "0.0", inclusive = false, message = "가격은 0보다 커야 합니다.")
    @Digits(integer = 8, fraction = 2, message = "가격 형식이 올바르지 않습니다.")
    private BigDecimal price;

    @Valid
    @NotEmpty(message = "최소 하나의 옵션은 필요합니다.")
    @Size(max = 50, message = "옵션은 최대 50개까지 등록 가능합니다.")
    private List<ProductOptionCreateRequest> options;



    // 옵션 중복 검증 메서드
    public boolean hasDuplicateOptions() {
        Set<String> optionKeys = new HashSet<>();
        return options.stream()
                .anyMatch(option -> !optionKeys.add(option.getColor() + "_" + option.getSize()));
    }

    // 사이즈 범위 검증 메서드
    public boolean hasValidSizeRange() {
        return options.stream()
                .allMatch(option -> option.getSize() >= 90 && option.getSize() <= 110);
    }
}
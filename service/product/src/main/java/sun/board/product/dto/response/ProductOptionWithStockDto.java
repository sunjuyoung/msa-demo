package sun.board.product.dto.response;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductOptionWithStockDto {
    private String color;
    private List<OptionSizeWithStockDto> sizes; // [{size:90, stock:10}, ...]
}
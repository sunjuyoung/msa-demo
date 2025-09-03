package sun.board.product.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OptionSizeWithStockDto {
    private Integer size;
    private Integer stock;
    private Long productOptionId;
}

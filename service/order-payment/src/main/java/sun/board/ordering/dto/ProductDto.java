package sun.board.ordering.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ProductDto {

    private Long productId;
    private String name;
    private int price;
    private int stockQuantity;
    private Long memberId;


}

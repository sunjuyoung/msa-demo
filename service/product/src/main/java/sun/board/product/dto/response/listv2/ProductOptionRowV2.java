package sun.board.product.dto.response.listv2;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductOptionRowV2 {
    private String color;
    private String size;
    private Long productOptionId;
}
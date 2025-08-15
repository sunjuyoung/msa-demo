package sun.board.product.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import sun.board.product.entity.Product;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ProductResDto {

    private Long productId;
    private String name;
    private int price;
    private int stockQuantity;
    private Long memberId;

    public static ProductResDto from(Product product) {
        return ProductResDto.builder()
                .productId(product.getId())
                .name(product.getName())
                .price(product.getPrice())
                .stockQuantity(product.getStockQuantity())
                .memberId(product.getMemberId())
                .build();
    }
}

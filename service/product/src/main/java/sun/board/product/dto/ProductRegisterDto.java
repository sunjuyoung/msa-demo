package sun.board.product.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import sun.board.product.entity.Product;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ProductRegisterDto {
    private String name;
    private String category;
    private int price;
    private int stockQuantity;

    public Product toEntity(Long userId){
        return Product.builder()
                .name(this.name).price(this.price).stockQuantity(this.stockQuantity)
                .memberId(userId)
                .build();
    }

}

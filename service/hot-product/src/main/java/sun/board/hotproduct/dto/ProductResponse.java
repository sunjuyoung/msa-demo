package sun.board.hotproduct.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductResponse {

    private Long id;
    private String name;
    private String description;
    private int price;
    private int stock;
    private String category;
    private int size;

    public static ProductResponse of(Long id, String name, String description, int price, int stock, String category, int size) {
        return ProductResponse.builder()
                .id(id)
                .name(name)
                .description(description)
                .price(price)
                .stock(stock)
                .category(category)
                .size(size)
                .build();
    }
}

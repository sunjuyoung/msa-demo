package sun.board.product.grpc.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;


public interface ProductGrpcResponse {

    Long getId();
    String getName();
    String getDescription();
    BigDecimal getPrice();
    String getCategory();
    String getColor();
    Integer getSize();
    Integer getStock();
}

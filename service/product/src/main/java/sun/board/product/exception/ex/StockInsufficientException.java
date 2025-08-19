package sun.board.product.exception.ex;

import lombok.Getter;

@Getter
public class StockInsufficientException extends RuntimeException {
    public StockInsufficientException(String message) {
        super(message);
    }


}

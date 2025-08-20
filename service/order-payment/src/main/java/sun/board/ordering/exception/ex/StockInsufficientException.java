package sun.board.ordering.exception.ex;

import lombok.Getter;

@Getter
public class StockInsufficientException extends RuntimeException {
    public StockInsufficientException(String message) {
        super(message);
    }
}

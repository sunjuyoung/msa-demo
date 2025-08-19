package sun.board.product.exception.ex;

import lombok.Getter;

@Getter
public class LockAcquisitionFailedException extends RuntimeException {
    public LockAcquisitionFailedException(String message) {
        super(message);
    }
}

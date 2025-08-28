package sun.board.payment.exception.ex;

import lombok.Getter;

@Getter
public class PSPConfirmationException extends RuntimeException {

    private String errorMessage;
    private boolean isRetryableError;
    public PSPConfirmationException(String message, boolean isRetryableError) {

        super(message);
        this.errorMessage = message;
        this.isRetryableError = isRetryableError;
    }
}

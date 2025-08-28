package sun.board.payment.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import sun.board.ordering.exception.ex.StockInsufficientException;
import sun.board.payment.exception.ex.PSPConfirmationException;
import sun.board.payment.exception.ex.PaymentAlreadyException;
import sun.board.payment.exception.ex.PaymentAmountException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(PaymentAmountException.class)
    public ResponseEntity<ErrorResponse> PaymentAmountException(PaymentAmountException ex) {
        log.error("---PaymentAmountException---");
        final ErrorResponse response = ErrorResponse.of(400, ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(PaymentAlreadyException.class)
    public ResponseEntity<ErrorResponse> PaymentAlreadyException(PaymentAlreadyException ex) {
        log.error("---PaymentAlreadyException---");
        final ErrorResponse response = ErrorResponse.of(400, ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    //PSPConfirmException
    @ExceptionHandler(PSPConfirmationException.class)
    public ResponseEntity<ErrorResponse> PSPConfirmException(PSPConfirmationException ex) {
        log.error("---PSPConfirmException---");
        final ErrorResponse response = ErrorResponse.of(400, ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(StockInsufficientException.class)
    public ResponseEntity<ErrorResponse> handleStockInsufficientException(StockInsufficientException e) {
        log.warn("재고 부족 예외 발생: {}", e.getMessage());
        final ErrorResponse response = ErrorResponse.of(400, e.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

}

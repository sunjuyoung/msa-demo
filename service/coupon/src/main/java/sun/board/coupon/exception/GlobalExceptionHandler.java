package sun.board.coupon.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import sun.board.coupon.exception.ex.AlreadyIssuedException;
import sun.board.coupon.exception.ex.CouponIssuePeriodException;
import sun.board.coupon.exception.ex.CouponLimitExceededException;
import sun.board.coupon.exception.ex.NonRetryableKafkaException;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CouponIssuePeriodException.class)
    public ResponseEntity<ErrorResponse> handleCouponIssuePeriodException(CouponIssuePeriodException ex) {
        log.error("Coupon issue period exception: {}", ex.getMessage());
        ErrorResponse errorResponse = new ErrorResponse(400, ex.getMessage());
        return ResponseEntity
                .status(400)
                .body(errorResponse);

    }

    @ExceptionHandler(AlreadyIssuedException.class)
    public ResponseEntity<ErrorResponse> handleAlreadyIssuedException(AlreadyIssuedException ex) {
        log.error("Already issued exception: {}", ex.getMessage());
        ErrorResponse errorResponse = new ErrorResponse(400, ex.getMessage());
        return ResponseEntity
                .status(400)
                .body(errorResponse);
    }

    @ExceptionHandler(CouponLimitExceededException.class)
    public ResponseEntity<ErrorResponse> handleCouponLimitExceededException(CouponLimitExceededException ex) {
        log.error("Coupon limit exceeded exception: {}", ex.getMessage());
        ErrorResponse errorResponse = new ErrorResponse(400, ex.getMessage());
        return ResponseEntity
                .status(400)
                .body(errorResponse);
    }

    @ExceptionHandler(NonRetryableKafkaException.class)
    public ResponseEntity<ErrorResponse> handleNonRetryableKafkaException(NonRetryableKafkaException ex) {
        log.error("Non-retryable Kafka exception: {}", ex.getMessage());
        ErrorResponse errorResponse = new ErrorResponse(500, "Internal server error");
        return ResponseEntity
                .status(500)
                .body(errorResponse);
    }

    // 예외 응답을 위한 공통 DTO
    public record ErrorResponse(int status, String message) {
    }

}


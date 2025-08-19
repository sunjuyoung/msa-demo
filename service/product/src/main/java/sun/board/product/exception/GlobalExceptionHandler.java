package sun.board.product.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import sun.board.product.exception.ex.LockAcquisitionFailedException;
import sun.board.product.exception.ex.StockInsufficientException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 재고 부족 예외 처리 핸들러
     * HTTP 상태 코드 400 Bad Request와 함께 에러 메시지 반환
     */
    @ExceptionHandler(StockInsufficientException.class)
    public ResponseEntity<ErrorResponse> handleStockInsufficientException(StockInsufficientException e) {
        log.warn("재고 부족 예외 발생: {}", e.getMessage());
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), "재고가 부족합니다.");
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    /**
     * 분산 락 획득 실패 예외 처리 핸들러
     * HTTP 상태 코드 503 Service Unavailable과 함께 에러 메시지 반환
     */
    @ExceptionHandler(LockAcquisitionFailedException.class)
    public ResponseEntity<ErrorResponse> handleLockAcquisitionFailedException(LockAcquisitionFailedException e) {
        log.error("분산 락 획득 실패: {}", e.getMessage(), e);
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.SERVICE_UNAVAILABLE.value(), "시스템 오류로 인해 주문 처리에 실패했습니다. 잠시 후 다시 시도해주세요.");
        return new ResponseEntity<>(errorResponse, HttpStatus.SERVICE_UNAVAILABLE);
    }

    /**
     * 모든 종류의 예외를 처리하는 핸들러 (폴백)
     * HTTP 상태 코드 500 Internal Server Error와 함께 일반적인 에러 메시지 반환
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleAllExceptions(Exception e) {
        log.error("알 수 없는 서버 오류 발생: {}", e.getMessage(), e);
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), "서버 내부 오류가 발생했습니다. 관리자에게 문의하세요.");
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // 예외 응답을 위한 공통 DTO
    public record ErrorResponse(int status, String message) {
    }
}
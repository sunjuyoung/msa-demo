package sun.board.coupon.exception.ex;

import lombok.Getter;

@Getter
public class NonRetryableKafkaException extends RuntimeException {



    public NonRetryableKafkaException(String message) {
        super(message);
    }
}

package sun.board.ordering.dto;

import lombok.*;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@Builder
@NoArgsConstructor
public class ApiResponse <T>{

    private String message;
    private T data;
    private int status;

    public ApiResponse(String message, T data, int status) {
        this.message = message;
        this.data = data;
        this.status = status;
    }

    public static <T> ApiResponse<T> success(T data) {
        return ApiResponse.<T>builder()
                .status(HttpStatus.OK.value())
                .message("success")
                .data(data)
                .build();
    }

    public static <T> ApiResponse<T> error(String message, int status) {
        return ApiResponse.<T>builder()
                .status(status)
                .message(message)
                .build();
    }
}

package uz.com.onlineshop.standard;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class StandardResponse<T> {

    private Status status;

    private String message;

    private T data;

    public static <T> StandardResponse<T> ok(String message, T data) {
        return StandardResponse.<T>builder()
                .status(Status.SUCCESS)
                .message(message)
                .data(data)
                .build();
    }
}

package place.reservationpay.common.dto;

import lombok.Getter;
import lombok.ToString;
import org.springframework.validation.FieldError;
import place.reservationpay.common.error.ErrorCode;

import java.util.List;

@ToString
@Getter
public class ApiResponse<T> {
    private final T data;
    private final boolean success;
    private final String code;
    private final String message;
    private List<FieldError> details;

    public ApiResponse(boolean success, String code, String message, List<FieldError> details) {
        this.data = null;
        this.success = success;
        this.code = code;
        this.message = message;
        this.details = details;
    }

    public ApiResponse(String code, String message) {
        this.data = null;
        this.success = false;
        this.code = code;
        this.message = message;
    }

    public ApiResponse(T data, String message) {
        this.data = data;
        this.success = true;
        this.code = "200";
        this.message = message;
    }

    // validation 오류처리
    public static  ApiResponse validation(ErrorCode errorCode,List<FieldError> details) {
        return new ApiResponse(false,errorCode.getCode(), errorCode.getMessage(),details);
    }

    // 에러처리
    public static ApiResponse error(String code,String message) {
        return new ApiResponse(code, message);
    }
    public static <T>ApiResponse<T> success(T data, String message) {
        return new ApiResponse<>(data,message);
    }
}

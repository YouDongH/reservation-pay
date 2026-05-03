package place.reservationpay.common.advice;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import place.reservationpay.common.dto.ApiResponse;
import place.reservationpay.common.error.ErrorCode;

import java.util.List;

@RestControllerAdvice(basePackages = "place.reservationpay")
public class ApiExceptionHandler {
    // Validate처리
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse> handleException(MethodArgumentNotValidException e) {
        ErrorCode errorCode = ErrorCode.VALIDATION_ERROR;
        List<FieldError> FieldError = e.getBindingResult().getFieldErrors();

        ApiResponse body = ApiResponse.validation(errorCode, FieldError);
        return ResponseEntity.status(errorCode.getStatus()).body(body);
    }

    // IllegalArgumentException - 입력값 오류처리
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse> handleException(IllegalArgumentException e) {
        ApiResponse.error(ErrorCode.BAD_REQUEST);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
    //
    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ApiResponse> handleException(IllegalStateException e) {
        ApiResponse.error(ErrorCode.CONFLICT);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();

    }
}

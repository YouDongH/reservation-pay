package place.reservationpay.common.error;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
    VALIDATION_ERROR(HttpStatus.BAD_REQUEST,"M1400-1","입력 값에 오류가 있습니다."),
    BAD_REQUEST(HttpStatus.BAD_REQUEST,"M1400-2","필수 파라미터 값이 누락되었습니다."),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED,"A1401","로그인정보가 존재하지 않습니다."),
    FORBIDDEN(HttpStatus.FORBIDDEN,"A403","접근권한이 존재하지 않습니다."),
    CONFLICT(HttpStatus.CONFLICT,"M409","상태가 올바르지 않습니다."),
    DATA_ACCESS_ERROR(HttpStatus.FORBIDDEN, "S500-1", "데이터 처리에 실패하였습니다.")
    ;

    private final HttpStatus status;
    private final String code;
    private final String message;

    ErrorCode(HttpStatus status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }

    public String getMessage(Throwable e) {
        return e.getMessage();
    }

}

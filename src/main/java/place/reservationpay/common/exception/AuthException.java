package place.reservationpay.common.exception;

import lombok.Getter;
import place.reservationpay.common.error.ErrorCode;

public class AuthException extends RuntimeException {
    @Getter private final ErrorCode errorCode;

    public AuthException(String message) {
        super(message);
        this.errorCode = ErrorCode.UNAUTHORIZED;
    }
}

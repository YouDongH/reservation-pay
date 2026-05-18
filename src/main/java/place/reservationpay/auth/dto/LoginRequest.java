package place.reservationpay.auth.dto;

import jakarta.validation.constraints.NotEmpty;

public record LoginRequest(
        @NotEmpty String loginId,
        @NotEmpty String pw
) {
    public static LoginRequest of(String loginId, String pw) {
        return new LoginRequest(loginId, pw);
    }
}

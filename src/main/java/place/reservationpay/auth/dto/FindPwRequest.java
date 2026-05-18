package place.reservationpay.auth.dto;

import jakarta.validation.constraints.NotEmpty;

public record FindPwRequest(
        @NotEmpty String loginId,
        @NotEmpty String name,
        @NotEmpty String email
) {
    public static FindPwRequest of(String loginId, String name, String email){
        return new FindPwRequest(loginId, name, email);
    }
}

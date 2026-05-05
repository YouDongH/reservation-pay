package place.reservationpay.verification.dto;

import jakarta.validation.constraints.NotBlank;

public record SendVerificationRequest(
        @NotBlank String email,
        @NotBlank String verType
) {
}

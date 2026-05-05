package place.reservationpay.verification.dto;

import jakarta.validation.constraints.NotBlank;

public record CheckVerificationRequest(
        @NotBlank String email,
        @NotBlank String code
) {
}

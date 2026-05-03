package place.reservationpay.member.dto.request;

import jakarta.validation.constraints.NotEmpty;

public record EditMemberRequest(
        @NotEmpty String email,
        @NotEmpty String mobile
) {
}

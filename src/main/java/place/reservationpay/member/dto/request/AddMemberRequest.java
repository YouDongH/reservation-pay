package place.reservationpay.member.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import place.reservationpay.member.constant.Gender;

public record AddMemberRequest(
        @NotEmpty String loginId,
        @NotEmpty String pw,
        @NotEmpty String name,
        @NotEmpty String birthday,
        @NotNull Gender gender,
        @NotEmpty String email,
        @NotEmpty String mobile
) {
}

package place.reservationpay.member.dto.request;

import place.reservationpay.member.constant.Gender;

public record AddMemberRequest(
        String loginId,
        String pw,
        String birthday,
        Gender gender,
        String email,
        String mobile
) {
}

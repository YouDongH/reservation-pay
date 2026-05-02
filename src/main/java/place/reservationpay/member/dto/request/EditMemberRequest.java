package place.reservationpay.member.dto.request;

import place.reservationpay.member.constant.Gender;

public record EditMemberRequest(
        String email,
        String mobile
) {
}

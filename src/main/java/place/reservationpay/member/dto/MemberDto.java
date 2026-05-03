package place.reservationpay.member.dto;

import place.reservationpay.member.constant.Gender;
import place.reservationpay.member.domain.Member;

public record MemberDto(
        String loginId,
        String pw,
        String birthday,
        Gender gender,
        String email,
        String mobile
) {
    public static MemberDto from(Member member) {
        return new MemberDto(
                member.getLoginId(), member.getPw(), member.getBirthday(), member.getGender(), member.getEmail(), member.getMobile()
        );
    }
    public static MemberDto of(String loginId, String pw, String birthday, Gender gender, String email, String mobile) {
        return new MemberDto(loginId, pw, birthday, gender, email, mobile);
    }
}

package place.reservationpay.fixtures;

import place.reservationpay.member.constant.Gender;
import place.reservationpay.member.domain.Member;
import place.reservationpay.member.dto.MemberDto;
import place.reservationpay.member.dto.request.AddMemberRequest;
import place.reservationpay.member.dto.request.EditMemberRequest;

public class MemberFixtures {
    public static AddMemberRequest createAddMemberRequest() {
        return new AddMemberRequest(
                "test01","1234","2002-01-25", Gender.WOMAN,"email@naver.com","010-9999-8888"
        );
    }
    public static AddMemberRequest createAddMemberRequest(
            String loginId, String pw, String birthday, Gender gender, String email, String mobile
    ) {
        return new AddMemberRequest(loginId,pw,birthday,gender,email,mobile);
    }

    public static EditMemberRequest createEditMemberRequest() {
        return new EditMemberRequest(
                "email02@naver.com","010-1111-8888"
        );
    }
    public static Member createMember() {
        return Member.createMemberForTest(1L,"test01","1234","2002-01-25", Gender.WOMAN,"email@naver.com","010-9999-8888");
    }
    public static MemberDto createMemberDto() {
        return new MemberDto("test01","1234","2002-01-25", Gender.WOMAN,"email@naver.com","010-9999-8888");
    }
}

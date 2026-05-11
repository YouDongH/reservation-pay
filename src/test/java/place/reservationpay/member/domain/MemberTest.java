package place.reservationpay.member.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import place.reservationpay.member.constant.Gender;

import static org.assertj.core.api.Assertions.assertThat;

class MemberTest {

    @Test
    @DisplayName("회원 생성시 등록일 및 등급은 일반회원으로 생성된다.")
    void createMemberTest() {
        // given
        String loginId = "loginId";
        String pw = "pw";
        String name = "홍길동";
        String birthday = "2002-02-25";
        Gender gender = Gender.MAN;
        String email = "email@naver.com";
        String mobile = "010-6655-4433";
        // when
        Member result = Member.createMember(loginId, pw, name, birthday, gender, email, mobile);

        // then
        assertThat(result.getGrade()).isEqualTo("일반회원");
        assertThat(result.getCreateAt()).isNotNull();

    }
}
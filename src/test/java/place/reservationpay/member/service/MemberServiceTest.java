package place.reservationpay.member.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import place.reservationpay.fixtures.MemberFixtures;
import place.reservationpay.member.domain.Member;
import place.reservationpay.member.dto.MemberDto;
import place.reservationpay.member.dto.request.AddMemberRequest;
import place.reservationpay.member.dto.request.EditMemberRequest;
import place.reservationpay.member.repository.MemberRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {
    @InjectMocks private MemberService sut;
    @Mock private MemberRepository memberRepository;
    @Mock private PasswordEncoder passwordEncoder;

    @Nested
    @DisplayName("직원등록")
    class AddMember {
        @Test
        @DisplayName("직원 등록성공시 MemberDto반환")
        public void givenRequestWhenAddMemberThenReturnMemberDto() throws Exception {
            // given
            AddMemberRequest request = MemberFixtures.createAddMemberRequest();
            Member member = MemberFixtures.createMember();
            String encodedPassword = passwordEncoder.encode("1234");
            given(memberRepository.save(any(Member.class))).willReturn(member);
            given(passwordEncoder.encode(any())).willReturn(encodedPassword);
            // when
            MemberDto result = sut.addMember(request);
            // then
            assertThat(result).isNotNull();
            assertThat(result.loginId()).isEqualTo(request.loginId());
        }
        @Test
        @DisplayName("직원 등록시 비번 암호화")
        public void givenPasswordWhenAddMemberThenEncodePassword() throws Exception {
            // given
            AddMemberRequest request = MemberFixtures.createAddMemberRequest();
            String pw="1234";
            String encodedPassword = passwordEncoder.encode(pw);
            Member member = MemberFixtures.createMember(encodedPassword);
            given(memberRepository.save(any(Member.class))).willReturn(member);
            given(passwordEncoder.encode(any())).willReturn(encodedPassword);
            // when
            MemberDto result = sut.addMember(request);
            // then
            assertThat(result.pw()).isEqualTo(encodedPassword);
        }
    }

    @Nested
    @DisplayName("직원 정보수정")
    class EditMember {
        @Test
        @DisplayName("직원정보 수정 성공시 MemberDto반환")
        public void givenRequestWhenEditMemberThenReturnMemberDto() throws Exception {
            // given
            Long id = 1L;
            EditMemberRequest request = MemberFixtures.createEditMemberRequest();
            Member member = MemberFixtures.createMember();
            given(memberRepository.findById(id)).willReturn(Optional.of(member));
            // when
            MemberDto result = sut.editMember(id,request);
            // then
            assertThat(result).isNotNull();
            assertThat(result.email()).isEqualTo(request.email());
            assertThat(result.mobile()).isEqualTo(request.mobile());
        }

        @Test
        @DisplayName("수정할 회원 정보가 존재하지 않을경우 IllegalArgumentException throw")
        public void givenNotMemberWhenEditMemberThenThrowIllegalArgumentException() throws Exception {
            // given
            Long id = 1L;
            EditMemberRequest request = MemberFixtures.createEditMemberRequest();
            given(memberRepository.findById(id)).willReturn(Optional.empty());
            // when && then
            assertThatThrownBy(() -> sut.editMember(id,request))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("조회 결과 존재하지 않습니다.");
        }
    }

    @Nested
    @DisplayName("직원 등급 수정")
    class changeGrade {
        @Test
        @DisplayName("직원등급 변경 성공시 직원ID 반환")
        public void givenSuccessWhenChangeGradeThenReturnMemberId() throws Exception {
            // given
            Long id = 1L;
            String grade = "우수회원";
            Member member = MemberFixtures.createMember();
            given(memberRepository.findById(id)).willReturn(Optional.of(member));
            // when
            Long result = sut.changeGrade(id,grade);
            // then
            assertThat(result).isNotNull();
            assertThat(result).isEqualTo(id);
        }

        @Test
        @DisplayName("수정할 회원 정보가 존재하지 않을경우 IllegalArgumentException throw")
        public void givenNotMemberWhenChangeGradeThenThrowIllegalArgumentException() throws Exception {
            // given
            Long id = 1L;
            String grade = "우수회원";
            given(memberRepository.findById(id)).willReturn(Optional.empty());
            // when && then
            assertThatThrownBy(()->sut.changeGrade(id,grade))
                    .isInstanceOf(Exception.class)
                    .hasMessage("조회 결과 존재하지 않습니다.");
        }
    }

    @Nested
    @DisplayName("아이디 중복확인")
    class existId{
        @Test
        @DisplayName("아이디가 존재하지 않을 경우 LoginId 반환")
        public void givenNotExistIdWhenCheckLoginIdThenReturnLoginId() throws Exception {
            // given
            String loginId = "test01";
            given(memberRepository.existId(loginId)).willReturn(false);
            // when
            String result = sut.checkLoginId(loginId);
            // then
            assertThat(result).isEqualTo("test01");
        }
        @Test
        @DisplayName("아이디가 존재할 경우 IllegalStateException Throw")
        public void givenExistIdWhenCheckLoginIdThenThrowIllegalStateException() throws Exception {
            // given
            String loginId = "test01";
            given(memberRepository.existId(loginId)).willReturn(true);
            // when && then
            assertThatThrownBy(() -> sut.checkLoginId(loginId))
                    .isInstanceOf(IllegalStateException.class)
                    .hasMessage("이미 등록된 아이디입니다.");
        }
    }

    @Nested
    @DisplayName("회원 탈퇴")
    class removeMember{
        @Test
        @DisplayName("회원 탈퇴")
        public void givenSuccessWhenRemoveMemberThenVoid() throws Exception {
            // given
            Long id = 1L;
            willDoNothing().given(memberRepository).deleteById(id);
            // when
            sut.removeMember(id);
            // then
            verify(memberRepository).deleteById(id);
        }
    }

}
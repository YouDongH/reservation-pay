package place.reservationpay.member.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import place.reservationpay.fixtures.MemberFixtures;
import place.reservationpay.member.domain.Member;
import place.reservationpay.member.dto.MemberDto;
import place.reservationpay.member.dto.request.AddMemberRequest;
import place.reservationpay.member.repository.MemberRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {
    @InjectMocks private MemberService sut;
    @Mock private MemberRepository memberRepository;

    @Nested
    @DisplayName("직원등록")
    class AddMember {
        @Test
        @DisplayName("직원 등록성공시 MemberDto반환")
        public void givenRequestWhenAddMemberThenReturnMemberDto() throws Exception {
            // given
            AddMemberRequest request = MemberFixtures.createAddMemberRequest();
            Member member = MemberFixtures.createMember();
            given(memberRepository.save(any(Member.class))).willReturn(member);
            // when
            MemberDto result = sut.addMember(request);
            System.out.println("result = " + result);
            // then
            assertThat(result).isNotNull();
            assertThat(result.loginId()).isEqualTo(request.loginId());
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
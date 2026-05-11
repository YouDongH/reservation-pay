package place.reservationpay.auth.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import place.reservationpay.auth.dto.LoginRequest;
import place.reservationpay.auth.dto.LoginSessionDto;
import place.reservationpay.common.exception.AuthException;
import place.reservationpay.fixtures.AuthFixtures;
import place.reservationpay.member.repository.MemberRepository;
import place.reservationpay.member.repository.query.LoginVo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {
    @InjectMocks private AuthService sut;
    @Mock private MemberRepository memberRepository;
    @Mock private PasswordEncoder passwordEncoder;

    @Nested
    @DisplayName("[로그인검증]")
    class Login{
        @Test
        @DisplayName("비밀번호가 일치시 로그인아이디와 회원아이디를 반환")
        public void givenEqualPwWhenLoginThenReturnLoginIdAndMemberId() throws Exception {
            // given
            String pw ="1234";
            LoginRequest request = LoginRequest.of("test01",pw);
            LoginVo loginVo = AuthFixtures.createLoginVo();
            given(memberRepository.checkLoginInfo(any())).willReturn(loginVo);
            given(passwordEncoder.matches(any(),any())).willReturn(true);
            // when
            LoginSessionDto result = sut.login(request);
            // then
            assertThat(result.id()).isEqualTo(loginVo.id());
            assertThat(result.name()).isEqualTo(loginVo.name());
        }
        @Test
        @DisplayName("등록된 아이디가 존재하지 않을 경우 AuthException Throw")
        public void givenNotExistLoginIdWhenLoginThenReturnAuthExceptionThrow() throws Exception {
            // given
            String pw ="1234";
            LoginRequest request = LoginRequest.of("test02","1234");
            LoginVo loginVo = AuthFixtures.createLoginVo();
            given(memberRepository.checkLoginInfo(any())).willReturn(null);
            // when && then
            assertThatThrownBy(()->sut.login(request))
                            .isInstanceOf(AuthException.class)
                            .hasMessage("회원가입 정보가 없습니다.");
        }
        @Test
        @DisplayName("비밀번호가 일치하지 않을 경우 AuthException Throw")
        public void givenNotEqualPwWhenLoginThenReturnAuthExceptionThrow() throws Exception {
            // given
            String pw ="12345";
            LoginRequest request = LoginRequest.of("test01",pw);
            LoginVo loginVo = AuthFixtures.createLoginVo();
            given(memberRepository.checkLoginInfo(any())).willReturn(loginVo);
            given(passwordEncoder.matches(any(),any())).willReturn(false);
            // when && then
            assertThatThrownBy(()->sut.login(request))
                    .isInstanceOf(AuthException.class)
                    .hasMessage("비밀번호가 일치하지 않습니다.");
        }
    }

}
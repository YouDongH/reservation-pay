package place.reservationpay.verification.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import place.reservationpay.fixtures.VerificationFixtures;
import place.reservationpay.verification.constant.AuthFlag;
import place.reservationpay.verification.domain.Verification;
import place.reservationpay.verification.dto.CheckVerificationRequest;
import place.reservationpay.verification.dto.SendVerificationRequest;
import place.reservationpay.verification.repository.VerificationRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class VerificationServiceTest {
    @InjectMocks private VerificationService sut;
    @Mock private VerificationRepository verificationRepository;
    @Mock JavaMailSender javaMailSender;
    @Captor
    private ArgumentCaptor<Verification> verificationCaptor;

    @Nested
    @DisplayName("이메일 인증번호 전송 테스트")
    class SendCode{
        @Test
        @DisplayName("이메일 전송시 인증코드는 6자리이다.")
        public void EmailCodeLengthIs6() throws Exception {
            // given
            SendVerificationRequest request = new SendVerificationRequest("test01@naver.com","email");
            Verification verification = VerificationFixtures.createVerification();
            given(verificationRepository.save(any())).willReturn(verification);
            // when
            sut.sendVerificationCode(request);
            verify(verificationRepository).save(verificationCaptor.capture());

            Verification captorValue = verificationCaptor.getValue();
            // then
            assertThat(captorValue.getVerCode().length()).isEqualTo(6);
        }

        @Test
        @DisplayName("이메일 인증번호 전송시 메일이 전송된다.")
        public void givenSuccessWhenSendVerificationCodeWhenSendEmail() throws Exception {
            // given
            SendVerificationRequest request = new SendVerificationRequest("test01@naver.com","email");
            Verification verification = VerificationFixtures.createVerification();
            given(verificationRepository.save(any())).willReturn(verification);

            // when
            sut.sendVerificationCode(request);
            // then
            verify(javaMailSender).send(any(SimpleMailMessage.class));
        }
    }

    @Nested
    @DisplayName("이메일 인증번호 확인 테스트")
    class CheckCode{
        @Test
        @DisplayName("인증번호가 일치시 상태가 SUCCESS로 반환한다.")
        public void givenEqualedCodeWhenCheckCodeThenSuccessStatus() throws Exception {
            // given
            CheckVerificationRequest request = new CheckVerificationRequest("test01@naver.com","967586");
            Verification verification = VerificationFixtures.createVerification();
            given(verificationRepository.findTopByVerEmailOrderByExpiredTimeDesc(request.email())).willReturn(Optional.of(verification));
            // when
            AuthFlag authFlag = sut.checkVerification(request);
            // then
            assertThat(authFlag).isEqualTo(AuthFlag.SUCCESS);
        }
        @Test
        @DisplayName("인증번호가 일치하지 않으면가 FAIL로 변경된다.")
        public void givenNotEqualedCodeWhenCheckCodeThenFailStatus() throws Exception {
            // given
            CheckVerificationRequest request = new CheckVerificationRequest("test01@naver.com","967581");
            Verification verification = VerificationFixtures.createVerification();
            given(verificationRepository.findTopByVerEmailOrderByExpiredTimeDesc(request.email())).willReturn(Optional.of(verification));
            // when
            AuthFlag authFlag = sut.checkVerification(request);
            // then
            assertThat(authFlag).isEqualTo(AuthFlag.FAIL);
        }
        @Test
        @DisplayName("요청한 인증이 없을 경우 IllegalStateException Throw")
        public void givenNotExistSendRequestWhenCheckCodeThenThrowIllegalStateException() throws Exception {
            // given
            CheckVerificationRequest request = new CheckVerificationRequest("test01@naver.com","967581");
            given(verificationRepository.findTopByVerEmailOrderByExpiredTimeDesc(request.email()))
                    .willThrow(new IllegalStateException("요청한 인증이 없습니다."));
            // when && then
            assertThatThrownBy(()->sut.checkVerification(request))
                    .isInstanceOf(IllegalStateException.class)
                    .hasMessage("요청한 인증이 없습니다.");
        }

    }
}
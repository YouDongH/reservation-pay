package place.reservationpay.verification.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import place.reservationpay.fixtures.VerificationFixtures;
import place.reservationpay.verification.constant.AuthFlag;

import static org.assertj.core.api.Assertions.assertThat;

class VerificationTest {
    @Nested
    @DisplayName("인증상태 변경")
    class changeAuthFlag{

        @ParameterizedTest
        @CsvSource(value = {"true,SUCCESS","false,FAIL"})
        @DisplayName("인증상태가 대기일 경우 boolean 값에 따라 상태가 정해진다.")
        public void givenAuthFlagIsREADYAndParameterWhenChangeAuthFlagThenStatusChange(boolean flag, AuthFlag expectResult) throws Exception {
            // given
            Verification sut = VerificationFixtures.createVerification();
            // when
            sut.changeAuthFlag(flag);
            // then
            assertThat(sut.getAuthFlag()).isEqualTo(expectResult);
        }

        @Test
        @DisplayName("인증상태가 성공일 경우 IllegalStateException throw")
        public void test() throws Exception {
            // given
            Verification sut = VerificationFixtures.createSuccessVerification();
            // when && then
            Assertions.assertThatThrownBy(
                    () -> sut.changeAuthFlag(false)
            )
                    .isInstanceOf(IllegalStateException.class)
                    .hasMessage("이미 인증하였습니다.");

        }
    }
}
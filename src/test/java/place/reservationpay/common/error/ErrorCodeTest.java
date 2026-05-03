package place.reservationpay.common.error;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.arguments;

class ErrorCodeTest {
    @ParameterizedTest
    @MethodSource("testErrorCode")
    @DisplayName("에러코드 테스트")
    public void errorCodeTest(String name,ErrorCode sut, String expectCode, String expectMessage) throws Exception {
        // given
        // when
        String actualMessage = sut.getMessage();
        String actualCode = sut.getCode();
        // then
        assertThat(actualCode).isEqualTo(expectCode);
        assertThat(actualMessage).isEqualTo(expectMessage);
    }
    static Stream<Arguments> testErrorCode(){
        return Stream.of(
                arguments("BAD_REQUEST",ErrorCode.VALIDATION_ERROR,"M1400-1","입력 값에 오류가 있습니다."),
                arguments("VALIDATION_ERROR",ErrorCode.BAD_REQUEST,"M1400-2","필수 파라미터 값이 누락되었습니다."),
                arguments("UNAUTHORIZED",ErrorCode.UNAUTHORIZED,"A1401","로그인정보가 존재하지 않습니다."),
                arguments("FORBIDDEN",ErrorCode.FORBIDDEN,"A403","접근권한이 존재하지 않습니다."),
                arguments("DATA_ACCESS_ERROR",ErrorCode.DATA_ACCESS_ERROR,"S500-1","데이터 처리에 실패하였습니다.")
        );
    }

}
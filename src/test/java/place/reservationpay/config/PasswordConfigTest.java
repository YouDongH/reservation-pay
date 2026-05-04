package place.reservationpay.config;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class PasswordConfigTest {

    @Test
    @DisplayName("비밀번호 암호화 테스트")
    public void pwTest() throws Exception {
        // given
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String pw = "1234";
        // when
        String encodePw = passwordEncoder.encode(pw);
        // then
        assert encodePw != null;
        assertThat(encodePw.equals(pw)).isFalse();
        assertThat(passwordEncoder.matches(pw, encodePw)).isTrue();
    }
}
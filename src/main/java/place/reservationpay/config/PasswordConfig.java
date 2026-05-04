package place.reservationpay.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class PasswordConfig {

    /**
     * PasswordEncoder
     * @Nullable String encode(@Nullable CharSequence rawPassword);
     * boolean matches(@Nullable CharSequence rawPassword, @Nullable String encodedPassword);
     * default boolean upgradeEncoding(@Nullable String encodedPassword) {
     * 		return false;
     * }
     *
     * **/

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

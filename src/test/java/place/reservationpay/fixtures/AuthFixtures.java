package place.reservationpay.fixtures;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import place.reservationpay.auth.dto.FindIdRequest;
import place.reservationpay.auth.dto.FindPwRequest;
import place.reservationpay.auth.dto.LoginSessionDto;
import place.reservationpay.member.repository.query.LoginVo;

public class AuthFixtures {
    public static LoginVo createLoginVo(){
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String pw = passwordEncoder.encode("1234");
        return LoginVo.of(1L,"test01", pw,"홍길동");
    }
    public static LoginSessionDto createLoginSessionDto(){
        return LoginSessionDto.of(1L,"홍길동");
    }
    public static FindIdRequest createFindIdRequest(){
        return FindIdRequest.of("홍길동","test01@naver.com");
    }
    public static FindPwRequest createFindPwRequest(){
        return FindPwRequest.of("test01","홍길동","test01@naver.com");
    }
}

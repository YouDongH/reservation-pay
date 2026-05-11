package place.reservationpay.auth.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import place.reservationpay.auth.dto.LoginRequest;
import place.reservationpay.auth.dto.LoginSessionDto;
import place.reservationpay.common.exception.AuthException;
import place.reservationpay.member.repository.MemberRepository;
import place.reservationpay.member.repository.query.LoginVo;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AuthService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    // 로그인 정보조회
    public LoginSessionDto login(LoginRequest request){
        LoginVo result = memberRepository.checkLoginInfo(request.loginId());
        if(result==null){
            throw new AuthException("회원가입 정보가 없습니다.");
        }
        if(!passwordEncoder.matches(request.pw(),result.pw())){
            throw new AuthException("비밀번호가 일치하지 않습니다.");
        }
        return LoginSessionDto.of(result.id(),result.name());
    }
    // 아이디 찾기

    // 비밀번호 찾기
}

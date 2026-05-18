package place.reservationpay.auth.controller;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import place.reservationpay.auth.dto.FindIdRequest;
import place.reservationpay.auth.dto.FindPwRequest;
import place.reservationpay.auth.dto.LoginRequest;
import place.reservationpay.auth.dto.LoginSessionDto;
import place.reservationpay.auth.service.AuthService;
import place.reservationpay.common.dto.ApiResponse;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AuthApiController {
    private final AuthService authService;

    @GetMapping("/login")
    public ApiResponse<Object> login(@RequestBody @Valid LoginRequest request, HttpSession session) {
        LoginSessionDto result = authService.login(request);
        session.setAttribute("id", result.id());
        session.setAttribute("name", result.name());

        return ApiResponse.success(null,"로그인에 성공하였습니다.");
    }
    @GetMapping("/logout")
    public ApiResponse<Object> logout(HttpSession session) {
        session.invalidate();
        return ApiResponse.success(null,"로그아웃에 성공하였습니다.");
    }

    @GetMapping("/find-id")
    public ApiResponse<Object> findId(@RequestBody @Valid FindIdRequest request) {
        String loginId = authService.findLoginId(request);

        return ApiResponse.success(loginId,"아이디찾기에 성공하였습니다.");
    }

    @GetMapping("/find-pw")
    public ApiResponse<Object> findPw(@RequestBody @Valid FindPwRequest request) {
        authService.findPw(request);

        return ApiResponse.success(null,"아이디찾기에 성공하였습니다.");
    }


}

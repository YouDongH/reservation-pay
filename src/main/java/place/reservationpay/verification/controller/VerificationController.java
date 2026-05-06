package place.reservationpay.verification.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import place.reservationpay.common.dto.ApiResponse;
import place.reservationpay.verification.constant.AuthFlag;
import place.reservationpay.verification.dto.CheckVerificationRequest;
import place.reservationpay.verification.dto.SendVerificationRequest;
import place.reservationpay.verification.service.VerificationService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class VerificationController {
    private final VerificationService verificationService;

    // 인증메일전송
    @PostMapping("/code/send")
    public ApiResponse<Object> sendVerificationCode(@Valid @RequestBody SendVerificationRequest request) {
        verificationService.sendVerificationCode(request);
        return ApiResponse.success(null,"인증메일이 발송되었씁니다.");
    }
    // 인증확인
    @PatchMapping("/code/check")
    public ApiResponse<AuthFlag> checkVerificationCode(@Valid @RequestBody CheckVerificationRequest request) {
        AuthFlag authFlag = verificationService.checkVerification(request);
        return ApiResponse.success(authFlag,"인증되었습니다.");
    }
}

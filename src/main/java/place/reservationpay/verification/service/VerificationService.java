package place.reservationpay.verification.service;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import place.reservationpay.verification.domain.Verification;
import place.reservationpay.verification.dto.SendVerificationRequest;
import place.reservationpay.verification.repository.VerificationRepository;

@Service
@Transactional
@RequiredArgsConstructor
public class VerificationService {
    private final VerificationRepository verificationRepository;
    private final JavaMailSender mailSender;

    // 이메일 코드 생성
    private String createCode(int length) {
        int bound = (int) Math.pow(10, length);
        int min = (int) Math.pow(10, length - 1);
        return String.valueOf((int)(Math.random()*(bound - min))+min);
    }

    // 이메일 인증번호 전송
    public void sendVerificationCode(SendVerificationRequest request){
        String code = createCode(6);

        Integer time = 5;
        Verification verification = Verification.createVerification(request.verType(), request.email(), code, time);
        Verification result = verificationRepository.save(verification);

        sendMail(request.email(), code);
    }

    // 이메일 전송
    private void sendMail(String email, String code){
        /**
         *  private @Nullable String from;
         * 	private @Nullable String replyTo;
         * 	private String @Nullable [] to;
         * 	private String @Nullable [] cc;
         * 	private String @Nullable [] bcc;
         *
         * 	private @Nullable Date sentDate;
         * 	private @Nullable String subject;
         * 	private @Nullable String text;
         * **/
        String title="[좌석예약] 이메일 인증코드";

        SimpleMailMessage message = new SimpleMailMessage();
        String text = "인증코드는 " + code + "입니다.";
        message.setTo(email);
        message.setSubject(title);
        message.setText(text);

        mailSender.send(message);
    }

}

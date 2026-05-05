package place.reservationpay.verification.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import place.reservationpay.verification.constant.AuthFlag;

import java.time.LocalDateTime;

@Getter
@ToString
@NoArgsConstructor(access= AccessLevel.PROTECTED)
@Entity
@Table(
        indexes = {
                @Index(name="idx_email",columnList = "ver_email")
        },
        uniqueConstraints = {
            @UniqueConstraint(columnNames = {"ver_email","expired_time"})
        }
)
public class Verification {
    @Id @GeneratedValue
    private Long verId;
    private String verType;
    private String verEmail;
    private String verCode;
    @Enumerated(EnumType.STRING)
    private AuthFlag authFlag;
    private LocalDateTime expiredTime;

    public Verification(String verType, String verEmail, String verCode, AuthFlag authFlag, LocalDateTime expiredTime) {
        this.verType = verType;
        this.verEmail = verEmail;
        this.verCode = verCode;
        this.authFlag = authFlag;
        this.expiredTime = expiredTime;
    }

    public static Verification createVerification(
            String ver_type, String ver_email, String ver_code, Integer time
    ){
        LocalDateTime expiredTime = LocalDateTime.now().plusMinutes(time);
        return new Verification(ver_type, ver_email, ver_code, AuthFlag.READY, expiredTime);
    }

    public void changeAuthFlag(boolean flag){

        if(authFlag.equals(AuthFlag.SUCCESS)){
            throw new IllegalStateException("이미 인증하였습니다.");
        }else if(authFlag.equals(AuthFlag.EXPIRED)) {
            throw new IllegalStateException("인증시간이 경과하였습니다.");
        }else if(expiredTime.isBefore(LocalDateTime.now())){
            authFlag = AuthFlag.EXPIRED;
        }
        else if(authFlag.equals(AuthFlag.READY) || authFlag.equals(AuthFlag.FAIL)){
            if(flag){
                authFlag = AuthFlag.SUCCESS;
            }else {
                authFlag = AuthFlag.FAIL;
            }
        }
    }
}

package place.reservationpay.fixtures;

import place.reservationpay.member.domain.Member;
import place.reservationpay.verification.domain.Verification;

public class VerificationFixtures {
    public static Verification createVerification() {
        return Verification.createVerification(
                "EMAIL","test01@naver.com","967586", 5
        );
    }
    public static Verification createSuccessVerification() {
        Verification verification = Verification.createVerification(
                "EMAIL", "test01@naver.com", "967586", 5
        );
        verification.changeAuthFlag(true);

        return verification;
    }
}

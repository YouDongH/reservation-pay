package place.reservationpay.auth.dto;

public record FindPwRequest(
        String loginId,
        String name,
        String email
) {
    public static FindPwRequest of(String loginId, String name, String email){
        return new FindPwRequest(loginId, name, email);
    }
}

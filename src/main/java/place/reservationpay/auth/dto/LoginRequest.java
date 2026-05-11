package place.reservationpay.auth.dto;

public record LoginRequest(
        String loginId,
        String pw
) {
    public static LoginRequest of(String loginId, String pw) {
        return new LoginRequest(loginId, pw);
    }
}

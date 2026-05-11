package place.reservationpay.auth.dto;

public record LoginSessionDto(
        Long id,
        String name
) {
    public static  LoginSessionDto of(Long id, String name) {
        return new LoginSessionDto(id, name);
    }
}

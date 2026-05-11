package place.reservationpay.member.repository.query;

public record LoginVo(
        Long id,
        String loginId,
        String pw,
        String name
) {
    public static LoginVo of(Long id, String loginId, String pw, String name){
        return new LoginVo(id, loginId, pw, name);
    }
}

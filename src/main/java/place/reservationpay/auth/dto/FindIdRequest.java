package place.reservationpay.auth.dto;

public record FindIdRequest(
        String name,
        String email
) {
    public static FindIdRequest of(String name,String email){
        return new FindIdRequest(name,email);
    }
}

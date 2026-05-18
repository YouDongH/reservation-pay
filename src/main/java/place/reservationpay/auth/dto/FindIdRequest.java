package place.reservationpay.auth.dto;

import jakarta.validation.constraints.NotEmpty;

public record FindIdRequest(
        @NotEmpty String name,
        @NotEmpty String email
) {
    public static FindIdRequest of(String name,String email){
        return new FindIdRequest(name,email);
    }
}

package place.reservationpay.reservation.dto;

import place.reservationpay.reservation.constant.ReservationStatus;

import java.time.LocalDateTime;

public record AddReservationRequest(
        LocalDateTime startTime,
        Integer hours,
        Integer resCount,
        Long memberId,
        Long roomId
) {
}

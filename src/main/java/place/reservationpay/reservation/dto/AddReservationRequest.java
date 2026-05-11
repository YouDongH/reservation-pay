package place.reservationpay.reservation.dto;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record AddReservationRequest(
        @NotNull LocalDateTime startTime,
        @NotNull Integer hours,
        @NotNull Integer resCount,
        @NotNull Long memberId,
        @NotNull Long roomId
) {
}

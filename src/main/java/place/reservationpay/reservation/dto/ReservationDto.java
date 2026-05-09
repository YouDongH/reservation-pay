package place.reservationpay.reservation.dto;

import place.reservationpay.reservation.constant.ReservationStatus;
import place.reservationpay.reservation.domain.Reservation;

import java.time.LocalDateTime;

public record ReservationDto(
        Long id,
        String resNum,
        LocalDateTime startTime,
        LocalDateTime endTime,
        ReservationStatus status,
        LocalDateTime expireTime,
        Integer resCount,
        Long memberId,
        Long roomId,
        String roomName
) {
    public static ReservationDto from(Reservation r) {
        return new ReservationDto(
                r.getId(),r.getResNum(),r.getStartTime(),r.getEndTime(),r.getStatus(),r.getExpireTime(),r.getResCount(),r.getMember().getId(),r.getRoom().getId(),r.getRoom().getRoomName()
        );
    }
}

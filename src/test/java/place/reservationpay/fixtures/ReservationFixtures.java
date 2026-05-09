package place.reservationpay.fixtures;

import place.reservationpay.member.domain.Member;
import place.reservationpay.place.domain.Room;
import place.reservationpay.reservation.constant.ReservationStatus;
import place.reservationpay.reservation.domain.Reservation;
import place.reservationpay.reservation.dto.AddReservationRequest;

import java.time.LocalDateTime;

public class ReservationFixtures {
    public static Reservation createReservation(Member member, Room room) {
        return Reservation.createReservationForTest(
                1L,
                "20251111",
                LocalDateTime.of(2025, 1, 25, 11, 0),
                LocalDateTime.of(2025, 1, 25, 13, 0),
                LocalDateTime.of(2025, 1, 26, 23, 59),
                4, member, room

        );
    }

    public static Reservation createCompleteReservation(Member member, Room room) {
        Reservation reservation = Reservation.createReservationForTest(
                1L,
                "20251111",
                LocalDateTime.of(2025, 1, 25, 11, 0),
                LocalDateTime.of(2025, 1, 25, 13, 0),
                LocalDateTime.of(2025, 1, 26, 23, 59),
                4, member, room

        );
        reservation.changeStatus(ReservationStatus.COMPLETED);
        return reservation;
    }
    public static AddReservationRequest createAddReservationRequest() {
        return new AddReservationRequest(
                LocalDateTime.of(2025, 1, 25, 11, 0), 2, 4, 1L,1L
        );
    }
}

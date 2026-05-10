package place.reservationpay.reservation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import place.reservationpay.member.domain.Member;
import place.reservationpay.reservation.domain.Reservation;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation,Long> {
    Long countByCreateAt(LocalDate createAt);

    List<Reservation> findByEmployee(Member member);
}

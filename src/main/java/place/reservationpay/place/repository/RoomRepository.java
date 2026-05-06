package place.reservationpay.place.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import place.reservationpay.place.domain.Room;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {
}

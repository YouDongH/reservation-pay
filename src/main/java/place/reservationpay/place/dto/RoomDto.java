package place.reservationpay.place.dto;

import place.reservationpay.place.constant.RoomStatus;
import place.reservationpay.place.domain.Room;

import java.time.LocalTime;

public record RoomDto(
        Long id,
        String roomName,
        Integer capacity,
        Integer pricePerHour,
        RoomStatus status,
        LocalTime startTime,
        LocalTime endTime,
        String description
) {
    public static RoomDto from(Room r) {
        return new RoomDto(
                r.getId(), r.getRoomName(), r.getCapacity(), r.getPricePerHour(), r.getStatus(), r.getStartTime(), r.getEndTime(), r.getDescription()
        );
    }
}

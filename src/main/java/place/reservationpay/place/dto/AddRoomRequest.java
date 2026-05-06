package place.reservationpay.place.dto;

import java.time.LocalTime;

public record AddRoomRequest(
        String roomName,
        Integer capacity,
        Integer pricePerHour,
        LocalTime startTime,
        LocalTime endTime,
        String description
) {
}

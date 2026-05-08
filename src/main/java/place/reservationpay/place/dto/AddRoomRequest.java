package place.reservationpay.place.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalTime;

public record AddRoomRequest(
        @NotBlank String roomName,
        Integer capacity,
        Integer pricePerHour,
        @NotNull LocalTime startTime,
        @NotNull LocalTime endTime,
        String description
) {
}

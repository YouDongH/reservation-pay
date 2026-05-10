package place.reservationpay.reservation.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import place.reservationpay.common.dto.ApiResponse;
import place.reservationpay.reservation.dto.AddReservationRequest;
import place.reservationpay.reservation.dto.ReservationDto;
import place.reservationpay.reservation.service.ReservationService;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ReservationController {
    private final ReservationService reservationService;

    @PostMapping("/reservation")
    public ApiResponse<ReservationDto> addReservation(@Valid @RequestBody AddReservationRequest addReservationRequest) {
        ReservationDto response = reservationService.addReservation(addReservationRequest);
        return ApiResponse.success(response,"조회에 성공하였습니다.");
    }
}

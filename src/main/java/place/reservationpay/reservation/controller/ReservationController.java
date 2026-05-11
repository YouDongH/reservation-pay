package place.reservationpay.reservation.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import place.reservationpay.common.dto.ApiResponse;
import place.reservationpay.reservation.constant.ReservationStatus;
import place.reservationpay.reservation.dto.AddReservationRequest;
import place.reservationpay.reservation.dto.ReservationDto;
import place.reservationpay.reservation.service.ReservationService;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ReservationController {
    private final ReservationService reservationService;

    @GetMapping("/reservations")
    public ApiResponse< Page<ReservationDto>> getReservations(Long employeeId, Pageable pageable) {
        Page<ReservationDto> response = reservationService.getReservations(employeeId, pageable);
        return ApiResponse.success(response,"조회에 성공하였습니다.");
    }

    @PostMapping("/reservation")
    public ApiResponse<ReservationDto> addReservation(@Valid @RequestBody AddReservationRequest addReservationRequest) {
        ReservationDto response = reservationService.addReservation(addReservationRequest);
        return ApiResponse.success(response,"예약이 되었습니다.");
    }
    @PatchMapping("/reservation/{id}/cancel")
    public ApiResponse<Long> changeStatus(@PathVariable Long id) {
        Long response = reservationService.changeStatus(id, ReservationStatus.CANCELLED);
        return ApiResponse.success(response,"예약이 취소되었습니다.");
    }
}

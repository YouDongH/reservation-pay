package place.reservationpay.place.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import place.reservationpay.common.dto.ApiResponse;
import place.reservationpay.place.dto.AddRoomRequest;
import place.reservationpay.place.dto.RoomDto;
import place.reservationpay.place.service.RoomService;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class RoomController {
    private final RoomService roomService;

    @PostMapping("/room")
    public ApiResponse<RoomDto> addRoom(@Valid @RequestBody AddRoomRequest request) throws Exception {
        RoomDto response = roomService.addRoom(request);
        return ApiResponse.success(response,"등록에 성공하였습니다.");
    }
}

package place.reservationpay.place.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import place.reservationpay.common.dto.ApiResponse;
import place.reservationpay.place.constant.RoomStatus;
import place.reservationpay.place.dto.AddRoomRequest;
import place.reservationpay.place.dto.EditRoomRequest;
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
    @PatchMapping("/room/{id}")
    public ApiResponse<RoomDto> editRoom(@Valid @PathVariable Long id, @Valid @RequestBody EditRoomRequest request) throws Exception {
        RoomDto response = roomService.editRoom(id, request);
        return ApiResponse.success(response,"수정에 성공하였습니다.");
    }
    @PatchMapping("/room/{id}/change-status")
    public ApiResponse<Long> changeStatus(@PathVariable Long id, RoomStatus roomStatus) throws Exception {
        Long response = roomService.changeStatus(id, roomStatus);
        return ApiResponse.success(response,"상태변경에 성공하였습니다.");
    }
    @DeleteMapping("/room/{id}")
    public ApiResponse<Object> removeRoom(@PathVariable Long id){
        roomService.removeRoom(id);
        return ApiResponse.success(null,"삭제에 성공하였습니다.");
    }
}

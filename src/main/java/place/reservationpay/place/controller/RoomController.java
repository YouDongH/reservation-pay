package place.reservationpay.place.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    @GetMapping("/rooms")
    public ApiResponse<Page<RoomDto>> getAllRooms(Pageable pageable) {
        Page<RoomDto> response = roomService.getRooms(pageable);
        return ApiResponse.success(response,"조회에 성공하였습니다.");
    }

    @GetMapping("/room/{id}")
    public ApiResponse<RoomDto> getRoom(@PathVariable Long id) {
        RoomDto response = roomService.getRoom(id);
        return ApiResponse.success(response,"조회에 성공하였습니다.");
    }

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

package place.reservationpay.place.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import place.reservationpay.place.constant.RoomStatus;
import place.reservationpay.place.domain.Room;
import place.reservationpay.place.dto.AddRoomRequest;
import place.reservationpay.place.dto.EditRoomRequest;
import place.reservationpay.place.dto.RoomDto;
import place.reservationpay.place.repository.RoomRepository;

@Service
@Transactional
@RequiredArgsConstructor
public class RoomService {
    private final RoomRepository roomRepository;

    // 룸 목록 조회
    public Page<RoomDto> getRooms(Pageable pageable) {
        Page<Room> results = roomRepository.findByStatus(pageable);
        return results.map(RoomDto::from);
    }
    // 룸 상세조회
    public RoomDto getRoom(Long id) {
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("룸 정보가 존재하지 않습니다."));
        return RoomDto.from(room);
    }
    // 룸 등록
    public RoomDto addRoom(AddRoomRequest request) {
        Room room = Room.createRoom(request.roomName(), request.capacity(), request.pricePerHour(), request.startTime(), request.endTime(), request.description());
        Room result = roomRepository.save(room);
        return RoomDto.from(result);
    }
    // 룸 정보수정
    public RoomDto editRoom(Long id,EditRoomRequest request) {
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("등록된 룸 정보가 존재하지 않습니다."));
        room.updateRoom(
                request.roomName(),request.capacity(),request.pricePerHour(),request.startTime(),request.endTime(),request.description()
        );
        return RoomDto.from(room);
    }
    // 룸 상태 수정
    public Long changeStatus(Long id, RoomStatus status){
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("등록된 룸 정보가 존재하지 않습니다."));
        room.changeStatus(status);
        return id;
    }
    // 룸 삭제
    public void removeRoom(Long id) {
        this.roomRepository.deleteById(id);
    }
    
}

package place.reservationpay.place.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
    
}

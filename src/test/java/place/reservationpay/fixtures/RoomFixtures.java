package place.reservationpay.fixtures;

import place.reservationpay.place.constant.RoomStatus;
import place.reservationpay.place.domain.Room;
import place.reservationpay.place.dto.AddRoomRequest;
import place.reservationpay.place.dto.EditRoomRequest;
import place.reservationpay.place.dto.RoomDto;

import java.time.LocalTime;

public class RoomFixtures {
    public static AddRoomRequest createAddRoomRequest() {
        return new AddRoomRequest("1번 스터디룸", 4,10000, LocalTime.of(11,0),LocalTime.of(20,0),"");
    }
    public static EditRoomRequest createEditRoomRequest() {
        return new EditRoomRequest("1번 스터디룸", 4,10000, LocalTime.of(11,0),LocalTime.of(20,0),"");
    }
    public static Room createRoom(){
        return Room.createRoom(
                "1번 스터디룸", 4,10000, LocalTime.of(11,0),LocalTime.of(20,0),""
        );
    }

    public static AddRoomRequest createAddRoomRequest(String roomName, LocalTime startTime, LocalTime endTime) {
        return new AddRoomRequest(roomName, 4,10000, startTime,endTime,"");
    }
    public static RoomDto createRoomDto(){
        return new RoomDto(
                1L,"1번 스터디룸", 4,10000, RoomStatus.ACTIVE,LocalTime.of(11,0),LocalTime.of(20,0),""
        );
    }

    public static EditRoomRequest createEditRoomRequest(String roomName, LocalTime startTime, LocalTime endTime) {
        return new EditRoomRequest(roomName, 4,10000, startTime,endTime,"");
    }
}

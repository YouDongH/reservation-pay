package place.reservationpay.fixtures;

import place.reservationpay.place.domain.Room;
import place.reservationpay.place.dto.AddRoomRequest;
import place.reservationpay.place.dto.EditRoomRequest;

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
}

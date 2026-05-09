package place.reservationpay.place.domain;

import jakarta.persistence.*;
import lombok.*;
import place.reservationpay.place.constant.RoomStatus;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
@Entity
public class Room {
    @Id @GeneratedValue
    private Long id;
    @Column(nullable = false, unique = true)
    private String roomName;

    private Integer capacity;
    private Integer pricePerHour;
    @Enumerated(EnumType.STRING)
    private RoomStatus status;

    private LocalTime startTime;
    private LocalTime endTime;
    private String description;

    @Column(nullable = false, updatable = false)
    private LocalDate createAt;

    public Room(String roomName, Integer capacity, Integer pricePerHour, RoomStatus status, LocalTime startTime, LocalTime endTime, String description, LocalDate createAt) {
        this.roomName = roomName;
        this.capacity = capacity;
        this.pricePerHour = pricePerHour;
        this.status = status;
        this.startTime = startTime;
        this.endTime = endTime;
        this.description = description;
        this.createAt = createAt;
    }

    public Room(Long id, String roomName, Integer capacity, Integer pricePerHour, RoomStatus status, LocalTime startTime, LocalTime endTime, String description, LocalDate createAt) {
        this.id = id;
        this.roomName = roomName;
        this.capacity = capacity;
        this.pricePerHour = pricePerHour;
        this.status = status;
        this.startTime = startTime;
        this.endTime = endTime;
        this.description = description;
        this.createAt = createAt;
    }

    public static Room createRoom(
            String roomName, Integer capacity, Integer pricePerHour, LocalTime startTime, LocalTime endTime, String description
    ){
        if(capacity == null) capacity = 1;
        if(pricePerHour == null) pricePerHour = 0;
        return new Room(roomName,capacity,pricePerHour,RoomStatus.ACTIVE,startTime,endTime,description,LocalDate.now());
    }

    public void updateRoom(String roomName, Integer capacity, Integer pricePerHour, LocalTime startTime, LocalTime endTime, String description){
        this.roomName = roomName;
        this.capacity = capacity;
        this.pricePerHour = pricePerHour;
        this.startTime = startTime;
        this.endTime = endTime;
        this.description = description;
    }

    public void changeStatus(RoomStatus status){
        this.status = status;
    }

    public static Room createRoomForTest(
            Long id,String roomName, Integer capacity, Integer pricePerHour, LocalTime startTime, LocalTime endTime, String description
    ){
        if(capacity == null) capacity = 1;
        if(pricePerHour == null) pricePerHour = 0;
        return new Room(id,roomName,capacity,pricePerHour,RoomStatus.ACTIVE,startTime,endTime,description,LocalDate.now());
    }
}

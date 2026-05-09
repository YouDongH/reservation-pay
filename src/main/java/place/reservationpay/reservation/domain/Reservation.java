package place.reservationpay.reservation.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import place.reservationpay.member.domain.Member;
import place.reservationpay.place.constant.RoomStatus;
import place.reservationpay.place.domain.Room;
import place.reservationpay.reservation.constant.ReservationStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Reservation {
    @Id @GeneratedValue
    private Long id;

    @Column(unique = true, nullable = true)
    private String resNum;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    @Enumerated(EnumType.STRING)
    private ReservationStatus status;
    private LocalDateTime expireTime;
    private LocalDate createAt;
    private Integer resCount;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id")
    private Room room;

    public Reservation(String resNum, LocalDateTime startTime, LocalDateTime endTime, ReservationStatus status, LocalDateTime expireTime, LocalDate createAt, Integer resCount, Member member, Room room) {
        this.resNum = resNum;
        this.startTime = startTime;
        this.endTime = endTime;
        this.status = status;
        this.expireTime = expireTime;
        this.createAt = createAt;
        this.resCount = resCount;
        this.member = member;
        this.room = room;
    }

    public Reservation(Long id, String resNum, LocalDateTime startTime, LocalDateTime endTime, ReservationStatus status, LocalDateTime expireTime, LocalDate createAt, Integer resCount, Member member, Room room) {
        this.id = id;
        this.resNum = resNum;
        this.startTime = startTime;
        this.endTime = endTime;
        this.status = status;
        this.expireTime = expireTime;
        this.createAt = createAt;
        this.resCount = resCount;
        this.member = member;
        this.room = room;
    }

    // 생성
    public static Reservation createReservation(
            String resNum, LocalDateTime startTime, LocalDateTime endTime, LocalDateTime expireTime, Integer resCount, Member member, Room room
    ){
        return new Reservation(resNum,startTime,endTime,ReservationStatus.PENDING,expireTime,LocalDate.now(),resCount,member,room);
    }

    // 예약 상태 변경
    public void changeStatus(ReservationStatus status){
        if(this.status.equals(ReservationStatus.PENDING)){
            this.status = status;
        }else{
            throw new IllegalStateException("이미 처리된 예약은 수정이 불가능합니다.");
        }
    }

    public static Reservation createReservationForTest(
            Long id, String resNum, LocalDateTime startTime, LocalDateTime endTime, LocalDateTime expireTime, Integer resCount, Member member, Room room
    ){
        return new Reservation(id,resNum,startTime,endTime,ReservationStatus.PENDING,expireTime,LocalDate.now(),resCount,member,room);
    }
}

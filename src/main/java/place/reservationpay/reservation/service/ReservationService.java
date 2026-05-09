package place.reservationpay.reservation.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import place.reservationpay.member.domain.Member;
import place.reservationpay.member.repository.MemberRepository;
import place.reservationpay.place.domain.Room;
import place.reservationpay.place.repository.RoomRepository;
import place.reservationpay.reservation.domain.Reservation;
import place.reservationpay.reservation.dto.AddReservationRequest;
import place.reservationpay.reservation.dto.ReservationDto;
import place.reservationpay.reservation.repository.ReservationRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Service
@Transactional
@RequiredArgsConstructor
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final MemberRepository memberRepository;
    private final RoomRepository roomRepository;

    // 예약 조회

    // 예약 등록
    public ReservationDto addReservation(AddReservationRequest request) {
        String resNum = createResNum();
        System.out.println("resNum = " + resNum);
        LocalDateTime endTime = createEndTime(request.startTime(),request.hours());
        LocalDateTime expiredTime = createExpiredTime();
        Member member = memberRepository.findById(request.memberId())
                .orElseThrow(() -> new IllegalArgumentException("예약을 진행할 회원이 존재하지않습니다."));
        Room room = roomRepository.findById(request.roomId())
                .orElseThrow(() -> new IllegalArgumentException("예약 대상이 존재하지않습니다."));
        Reservation reservation = Reservation.createReservation(resNum, request.startTime(), endTime, expiredTime, request.resCount(), member, room);
        Reservation result = reservationRepository.save(reservation);
        return ReservationDto.from(result);
    }


    private String createResNum() {
        LocalDate now = LocalDate.now();
        Long count = reservationRepository.countByCreateAt(now) +1L;
        String format = String.format("%06d", count);
        return "RVC" + String.valueOf(now).replaceAll("-", "") + format;
    }
    private LocalDateTime createEndTime(LocalDateTime standardTime, Integer hours) {
        return standardTime.plusHours(hours);
    }
    private LocalDateTime createExpiredTime() {
        return LocalDateTime.of(LocalDate.now().plusDays(2), LocalTime.of(0,0));
    }


}

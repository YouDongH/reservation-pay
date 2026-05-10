package place.reservationpay.reservation.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import place.reservationpay.fixtures.MemberFixtures;
import place.reservationpay.fixtures.ReservationFixtures;
import place.reservationpay.fixtures.RoomFixtures;
import place.reservationpay.member.domain.Member;
import place.reservationpay.member.repository.MemberRepository;
import place.reservationpay.place.domain.Room;
import place.reservationpay.place.repository.RoomRepository;
import place.reservationpay.reservation.constant.ReservationStatus;
import place.reservationpay.reservation.domain.Reservation;
import place.reservationpay.reservation.dto.AddReservationRequest;
import place.reservationpay.reservation.dto.ReservationDto;
import place.reservationpay.reservation.repository.ReservationRepository;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;


@ExtendWith(MockitoExtension.class)
class ReservationServiceTest {
    @InjectMocks private ReservationService sut;

    @Mock private ReservationRepository reservationRepository;
    @Mock private MemberRepository memberRepository;
    @Mock private RoomRepository roomRepository;

    @Nested
    @DisplayName("[예약등록]")
    class AddReservation {
        @Test
        @DisplayName("예약생성 성공시 ReservationDto 반환")
        public void givenSuccessWhenAddReservationThenReturnReservationDto() throws Exception {
            // given
            AddReservationRequest request = ReservationFixtures.createAddReservationRequest();
            Member member = MemberFixtures.createMember();
            Room room = RoomFixtures.createRoom();
            Reservation reservation = ReservationFixtures.createReservation(member, room);
            given(reservationRepository.countByCreateAt(any())).willReturn(1L);
            given(memberRepository.findById(any())).willReturn(Optional.of(member));
            given(roomRepository.findById(any())).willReturn(Optional.of(room));
            given(reservationRepository.save(any())).willReturn(reservation);
            // when
            ReservationDto result = sut.addReservation(request);
            // then
            assertThat(result.resNum()).isNotNull();
        }

        @Test
        @DisplayName("회원정보가 존재하지 않을시 IllegalArgumentException Throw")
        public void givenNotMemberWhenAddReservationThenIllegalArgumentExceptionThrow() throws Exception {
            // given
            AddReservationRequest request = ReservationFixtures.createAddReservationRequest();
            given(reservationRepository.countByCreateAt(any())).willReturn(1L);
            given(memberRepository.findById(any())).willReturn(Optional.empty());
            // when && then
            assertThatThrownBy(() -> sut.addReservation(request))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("예약을 진행할 회원이 존재하지않습니다.");
        }

        @Test
        @DisplayName("룸 정보가 존재하지 않을시 IllegalArgumentException Throw")
        public void givenNotRoomWhenAddReservationThenIllegalArgumentExceptionThrow() throws Exception {
            // given
            AddReservationRequest request = ReservationFixtures.createAddReservationRequest();
            Member member = MemberFixtures.createMember();
            given(reservationRepository.countByCreateAt(any())).willReturn(1L);
            given(memberRepository.findById(any())).willReturn(Optional.of(member));
            given(roomRepository.findById(any())).willReturn(Optional.empty());
            // when && then
            assertThatThrownBy(() -> sut.addReservation(request))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("예약 대상이 존재하지않습니다.");
        }
    }
    @Nested
    @DisplayName("[예약상태 수정]")
    class ChangeReservationStatus {

        @Test
        @DisplayName("예약생성 성공시 Id 반환")
        public void givenSuccessWhenChangeReservationStatusThenReturnId() throws Exception {
            // given
            Member member = MemberFixtures.createMember();
            Room room = RoomFixtures.createRoom();
            Reservation reservation = ReservationFixtures.createReservation(member, room);
            given(reservationRepository.findById(any())).willReturn(Optional.of(reservation));
            // when
            Long result = sut.changeStatus(1L, ReservationStatus.COMPLETED);
            // then
            assertThat(result).isEqualTo(1L);
        }

        @Test
        @DisplayName("예약 정보가 존재하지 않을시 IllegalArgumentException Throw")
        public void givenNotReservationWhenChangeReservationStatusThenIllegalArgumentExceptionThrow() throws Exception {
            // given
            given(reservationRepository.findById(any())).willReturn(Optional.empty());
            // when && then
            assertThatThrownBy(() -> sut.changeStatus(1L, ReservationStatus.COMPLETED))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("예약정보가 존재하지 않습니다.");
        }

    }
}
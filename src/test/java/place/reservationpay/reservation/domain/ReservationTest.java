package place.reservationpay.reservation.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import place.reservationpay.fixtures.MemberFixtures;
import place.reservationpay.fixtures.ReservationFixtures;
import place.reservationpay.fixtures.RoomFixtures;
import place.reservationpay.member.domain.Member;
import place.reservationpay.place.domain.Room;
import place.reservationpay.reservation.constant.ReservationStatus;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ReservationTest {
    @Test
    @DisplayName("[예약생성] 등록일과 예약상태는 자동으로 입력된다.")
    public void givenSuccessWhenCreateReservationThenCreateAtAndStatusAuto() throws Exception {
        // given
        Member member = MemberFixtures.createMember();
        Room room = RoomFixtures.createRoom();
        // when
        Reservation sut = Reservation.createReservation(
                "20251111",
                LocalDateTime.of(2025, 1, 25, 11, 0),
                LocalDateTime.of(2025, 1, 25, 13, 0),
                LocalDateTime.of(2025, 1, 26, 23, 59),
                4, member, room

        );
        // then
        assertThat(sut.getStatus()).isEqualTo(ReservationStatus.PENDING);
        assertThat(sut.getCreateAt()).isNotNull();
    }
    @Test
    @DisplayName("[예약 상태변경] 입력한 값으로 상태가 변경됩니다.")
    public void givenStatusIsPendingWhenChangeStatusReservationThenChangeStatus() throws Exception {
        // given
        Member member = MemberFixtures.createMember();
        Room room = RoomFixtures.createRoom();
        Reservation sut = ReservationFixtures.createReservation(member,room);
        // when
        sut.changeStatus(ReservationStatus.COMPLETED);
        // then
        assertThat(sut.getStatus()).isEqualTo(ReservationStatus.COMPLETED);
    }
    @Test
    @DisplayName("[예약 상태변경] 결제대기가 아닌 경우 IllegalStateException Throw")
    public void givenNotPendingWhenChangeStatusReservationThenIllegalStateExceptionThrow() throws Exception {
        // given
        Member member = MemberFixtures.createMember();
        Room room = RoomFixtures.createRoom();
        Reservation sut = ReservationFixtures.createCompleteReservation(member,room);
        // when && then
        assertThatThrownBy(()->sut.changeStatus(ReservationStatus.EXPIRED))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("이미 처리된 예약은 수정이 불가능합니다.");

    }

}
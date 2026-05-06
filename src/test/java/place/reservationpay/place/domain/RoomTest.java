package place.reservationpay.place.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import place.reservationpay.place.constant.RoomStatus;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThat;

class RoomTest {

    @Test
    @DisplayName("생성시 룸상태와 생성일은 자동으로 입력된다.")
    public void givenNotStatusAndCreateAtWhenCreatRoomThenAuto() throws Exception {
        // given
        LocalDate createAt = LocalDate.now();
        // when
        Room sut = Room.createRoom("1번 스터디룸", 4, 10000, LocalTime.of(10, 0, 0), LocalTime.of(10, 0, 0), "테스트");
        // then
        assertThat(sut.getCreateAt()).isEqualTo(createAt);
        assertThat(sut.getStatus()).isEqualTo(RoomStatus.ACTIVE);
    }

    @Test
    @DisplayName("수용인원과 가격을 미입력시 각각 1, 0으로 입력된다.")
    public void givenNotStatusAndCreateAtWhenCreatRoomThenAuto1() throws Exception {
        // when
        Room sut = Room.createRoom("1번 스터디룸", null, null, LocalTime.of(10, 0, 0), LocalTime.of(10, 0, 0), "테스트");
        // then
        assertThat(sut.getCapacity()).isEqualTo(1);
        assertThat(sut.getPricePerHour()).isEqualTo(0);
    }
}
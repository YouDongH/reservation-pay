package place.reservationpay.place.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import place.reservationpay.fixtures.RoomFixtures;
import place.reservationpay.place.domain.Room;
import place.reservationpay.place.dto.AddRoomRequest;
import place.reservationpay.place.dto.EditRoomRequest;
import place.reservationpay.place.dto.RoomDto;
import place.reservationpay.place.repository.RoomRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class RoomServiceTest {
    @InjectMocks private RoomService sut;
    @Mock private RoomRepository roomRepository;

    @Nested
    @DisplayName("룸 등록")
    class AddRoom {
        @Test
        @DisplayName("룸 등록 성공시 Room 반환")
        public void givenSuccessWhenAddRoomThenReturnRoom() throws Exception {
            // given
            AddRoomRequest request = RoomFixtures.createAddRoomRequest();
            Room room = RoomFixtures.createRoom();
            given(roomRepository.save(any())).willReturn(room);
            // when
            RoomDto result = sut.addRoom(request);
            // then
            assertThat(result.roomName()).isEqualTo(request.roomName());
            assertThat(result.capacity()).isEqualTo(request.capacity());
            assertThat(result.startTime()).isEqualTo(request.startTime());
        }
    }

    @Nested
    @DisplayName("룸 정보수정")
    class EditRoom {
        @Test
        @DisplayName("수정 성공시 Room 반환")
        public void givenSuccessWhenEditRoomThenReturnRoom() throws Exception {
            // given
            Long id = 1L;
            EditRoomRequest request = RoomFixtures.createEditRoomRequest();
            Room room = RoomFixtures.createRoom();
            given(roomRepository.findById(any())).willReturn(Optional.of(room));
            // when
            RoomDto result = sut.editRoom(id,request);
            // then
            assertThat(result.roomName()).isEqualTo(request.roomName());
            assertThat(result.capacity()).isEqualTo(request.capacity());
            assertThat(result.startTime()).isEqualTo(request.startTime());
        }

        @Test
        @DisplayName("수정할 룸 정보가 존재하지 않을시 IllegalArgumentException Throw")
        public void givenNotExistRoomWhenEditRoomThenReturnIllegalArgumentExceptionThrow() throws Exception {
            // given
            Long id = 1L;
            EditRoomRequest request = RoomFixtures.createEditRoomRequest();
            given(roomRepository.findById(any())).willReturn(Optional.empty());
            // when && then
            assertThatThrownBy(()->sut.editRoom(id,request))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("등록된 룸 정보가 존재하지 않습니다.");
        }
    }

}
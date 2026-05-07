package place.reservationpay.place.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import place.reservationpay.fixtures.RoomFixtures;
import place.reservationpay.place.constant.RoomStatus;
import place.reservationpay.place.domain.Room;
import place.reservationpay.place.dto.AddRoomRequest;
import place.reservationpay.place.dto.EditRoomRequest;
import place.reservationpay.place.dto.RoomDto;
import place.reservationpay.place.repository.RoomRepository;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class RoomServiceTest {
    @InjectMocks private RoomService sut;
    @Mock private RoomRepository roomRepository;

    @Nested
    @DisplayName("룸 목록 조회")
    class GetRooms{
        @Test
        @DisplayName("조회 성공시 Room 반환")
        public void givenSuccessWhenGetRoomThenReturnRoom() throws Exception {
            // given
            Long id = 1L;
            Room room = RoomFixtures.createRoom();
            PageImpl<Room> rooms = new PageImpl<>(List.of(room));
            given(roomRepository.findByStatus(any())).willReturn(rooms);
            // when
            Pageable pageable = PageRequest.of(0, 10);
            Page<RoomDto> result = sut.getRooms(pageable);
            // then
            assertThat(result.getSize()).isEqualTo(1);
        }
    }

    @Nested
    @DisplayName("룸 상세정보 조회")
    class GetRoom{
        @Test
        @DisplayName("조회 성공시 Room 반환")
        public void givenSuccessWhenGetRoomThenReturnRoom() throws Exception {
            // given
            Long id = 1L;
            Room room = RoomFixtures.createRoom();
            given(roomRepository.findById(any())).willReturn(Optional.of(room));
            // when
            RoomDto result = sut.getRoom(id);
            // then
            assertThat(result.roomName()).isEqualTo(room.getRoomName());
            assertThat(result.capacity()).isEqualTo(room.getCapacity());
            assertThat(result.startTime()).isEqualTo(room.getStartTime());
        }

        @Test
        @DisplayName("수정할 룸 정보가 존재하지 않을시 IllegalArgumentException Throw")
        public void givenNotExistRoomWhenEditRoomThenReturnIllegalArgumentExceptionThrow() throws Exception {
            // given
            Long id = 1L;
            given(roomRepository.findById(any())).willReturn(Optional.empty());
            // when && then
            assertThatThrownBy(()->sut.getRoom(id))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("룸 정보가 존재하지 않습니다.");
        }
    }

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
    @Nested
    @DisplayName("룸 상태변경")
    class ChangeRoomStatus {
        @Test
        @DisplayName("상태변경 성공시 RoomId 반환")
        public void givenSuccessWhenChangeRoomStatusThenReturnRoomId() throws Exception {
            // given
            Long id = 1L;
            RoomStatus roomStatus = RoomStatus.ACTIVE;
            Room room = RoomFixtures.createRoom();
            given(roomRepository.findById(any())).willReturn(Optional.of(room));
            // when
            Long result = sut.changeStatus(id, roomStatus);
            // then
            assertThat(result).isEqualTo(id);
        }

        @Test
        @DisplayName("수정할 룸 정보가 존재하지 않을시 IllegalArgumentException Throw")
        public void givenNotExistRoomWhenChangeRoomStatusThenReturnIllegalArgumentExceptionThrow() throws Exception {
            // given
            Long id = 1L;
            RoomStatus roomStatus = RoomStatus.ACTIVE;
            given(roomRepository.findById(any())).willReturn(Optional.empty());
            // when && then
            assertThatThrownBy(()->sut.changeStatus(id,roomStatus))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("등록된 룸 정보가 존재하지 않습니다.");
        }
    }

    @Test
    @DisplayName("룸 삭제 테스트")
    public void removeRoomTest() throws Exception {
        // given
        Long id = 1L;
        // when
        sut.removeRoom(id);
        // then
        verify(roomRepository).deleteById(id);
    }

}
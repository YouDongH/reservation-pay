package place.reservationpay.place.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import place.reservationpay.fixtures.RoomFixtures;
import place.reservationpay.place.constant.RoomStatus;
import place.reservationpay.place.domain.Room;
import place.reservationpay.place.dto.AddRoomRequest;
import place.reservationpay.place.dto.EditRoomRequest;
import place.reservationpay.place.dto.RoomDto;
import place.reservationpay.place.service.RoomService;
import tools.jackson.databind.ObjectMapper;

import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RoomController.class)
class RoomControllerTest {
    @MockitoBean private RoomService roomService;

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;

    @Nested
    @DisplayName("[룸 조회][GET] /api/rooms")
    class GetRooms {
        @Test
        @DisplayName("룸 목록 조회 성공시 200 OK")
        public void givenSuccessWhenGetRoomsThenResponse200OK() throws Exception {
            // given
            PageRequest pageable = PageRequest.of(0, 10);
            RoomDto room = RoomFixtures.createRoomDto();
            PageImpl<RoomDto> response = new PageImpl<>(List.of(room));
            given(roomService.getRooms(any())).willReturn(response);
            // when && then
            mockMvc.perform(
                    get("/api/rooms")
                            .param("page", "0")
                            .param("size", "10")
            )
                    .andDo(print())
                    .andExpectAll(
                            status().isOk(),
                            jsonPath("$.data.content.size()").value(1),
                            jsonPath("$.message").value("조회에 성공하였습니다.")
                    );
            verify(roomService).getRooms(pageable);
        }
    }

    @Nested
    @DisplayName("[룸 조회][GET] /api/room")
    class GetRoom {
        @Test
        @DisplayName("룸 상세정보 조회 성공시 200 OK")
        public void givenSuccessWhenGetRoomThenResponse200OK() throws Exception {
            // given
            RoomDto room = RoomFixtures.createRoomDto();
            given(roomService.getRoom(any())).willReturn(room);
            // when && then
            mockMvc.perform(
                    get("/api/room/{id}",1L)
            )
                    .andDo(print())
                    .andExpectAll(
                            status().isOk(),
                            jsonPath("$.data.roomName").value("1번 스터디룸"),
                            jsonPath("$.message").value("조회에 성공하였습니다.")
                    );
            verify(roomService).getRoom(1L);
        }

        @Test
        @DisplayName("룸 상세정보 조회 실패시 400 BadRequest")
        public void givenNotExistRoomWhenGetRoomThenResponse400BadRequest() throws Exception {
            // given
            RoomDto room = RoomFixtures.createRoomDto();
            given(roomService.getRoom(any())).willThrow(new IllegalArgumentException("룸 정보가 존재하지 않습니다."));
            // when && then
            mockMvc.perform(
                            get("/api/room/{id}",1L)
                    )
                    .andDo(print())
                    .andExpectAll(
                            status().isBadRequest(),
                            jsonPath("$.message").value("룸 정보가 존재하지 않습니다.")
                    );
            verify(roomService).getRoom(1L);
        }
    }

    @Nested
    @DisplayName("[룸정보 등록][POST] /api/room")
    class AddRoom{
        @Test
        @DisplayName("룸 등록 성공시 200 OK")
        public void givenSuccessWhenAddRoomThen200Ok() throws Exception {
            // given
            AddRoomRequest request = RoomFixtures.createAddRoomRequest();
            RoomDto roomDto = RoomFixtures.createRoomDto();
            given(roomService.addRoom(any())).willReturn(roomDto);
            String body = objectMapper.writeValueAsString(request);
            // when
            mockMvc.perform(
                    post("/api/room")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(body)
            )
                    .andDo(print())
                    .andExpectAll(
                            status().isOk(),
                            jsonPath("$.data.id").value(1L),
                            jsonPath("$.data.roomName").value(roomDto.roomName())
                    );
            // then
            verify(roomService).addRoom(request);
        }
        @ParameterizedTest
        @CsvSource(value = {
                "nl,11:00:00,22:00:00",
                "1번 스터디룸,nl,22:00:00",
                "1번 스터디룸,11:00:00,nl"
        },nullValues = "nl")
        @DisplayName("등록정보 누락시 400 BadRequest")
        public void givenOmittedDataWhenAddRoomThen400BADREQUEST(
                String roomName, LocalTime startTime, LocalTime endTime
        ) throws Exception {
            // given
            AddRoomRequest request = RoomFixtures.createAddRoomRequest(roomName,startTime,endTime);
            String body = objectMapper.writeValueAsString(request);
            // when && then
            mockMvc.perform(
                            post("/api/room")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(body)
                    )
                    .andDo(print())
                    .andExpectAll(
                            status().isBadRequest()
                    );
        }
    }

    @Nested
    @DisplayName("[룸정보 수정][PATCH] /api/room/{id}")
    class EditRoom {
        @Test
        @DisplayName("룸 등록 성공시 200 OK")
        public void givenSuccessWhenEditRoomThen200Ok() throws Exception {
            // given
            EditRoomRequest request = RoomFixtures.createEditRoomRequest();
            RoomDto roomDto = RoomFixtures.createRoomDto();
            given(roomService.editRoom(any(),any())).willReturn(roomDto);
            String body = objectMapper.writeValueAsString(request);
            // when
            mockMvc.perform(
                            patch("/api/room/{id}",1L)
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(body)
                    )
                    .andDo(print())
                    .andExpectAll(
                            status().isOk(),
                            jsonPath("$.data.id").value(1L),
                            jsonPath("$.data.roomName").value(roomDto.roomName())
                    );
            // then
            verify(roomService).editRoom(1L,request);
        }
        @ParameterizedTest
        @CsvSource(value = {
                "nl,11:00:00,22:00:00",
                "1번 스터디룸,nl,22:00:00",
                "1번 스터디룸,11:00:00,nl"
        },nullValues = "nl")
        @DisplayName("등록정보 누락시 400 BadRequest")
        public void givenOmittedDataWhenEditRoomThen400BADREQUEST(
                String roomName, LocalTime startTime, LocalTime endTime
        ) throws Exception {
            // given
            EditRoomRequest request = RoomFixtures.createEditRoomRequest(roomName, startTime, endTime);
            String body = objectMapper.writeValueAsString(request);
            // when && then
            mockMvc.perform(
                            patch("/api/room/{id}",1L)
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(body)
                    )
                    .andDo(print())
                    .andExpectAll(
                            status().isBadRequest()
                    );
        }
        @Test
        @DisplayName("수정할 룸정보가 존재하지 않을시 400 BadRequest")
        public void givenNotEditRoomWhenEditRoomThen400BadRequest() throws Exception {
            // given
            EditRoomRequest request = RoomFixtures.createEditRoomRequest();
            given(roomService.editRoom(any(),any())).willThrow(new IllegalArgumentException("등록된 룸 정보가 존재하지 않습니다."));
            String body = objectMapper.writeValueAsString(request);
            // when
            mockMvc.perform(
                            patch("/api/room/{id}",1L)
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(body)
                    )
                    .andDo(print())
                    .andExpectAll(
                            status().isBadRequest(),
                            jsonPath("$.message").value("등록된 룸 정보가 존재하지 않습니다.")
                    );
            // then
            verify(roomService).editRoom(1L,request);
        }

    }
    @Nested
    @DisplayName("[룸상태 수정][PATCH] /api/room/{id}/change-status")
    class ChangeStatus {

        @Test
        @DisplayName("룸상태 수정에 성공시 200 OK")
        public void givenSuccessWhenChangeStatusThen200Ok() throws Exception {
            // given
            Long id = 1L;
            RoomStatus roomStatus = RoomStatus.INACTIVE;
            RoomDto roomDto = RoomFixtures.createRoomDto();
            given(roomService.changeStatus(any(),any())).willReturn(id);
            // when
            mockMvc.perform(
                            patch("/api/room/{id}/change-status",id)
                                    .param("roomStatus",roomStatus.toString())
                    )
                    .andDo(print())
                    .andExpectAll(
                            status().isOk(),
                            jsonPath("$.data").value(1L),
                            jsonPath("$.message").value("상태변경에 성공하였습니다.")
                    );
            // then
            verify(roomService).changeStatus(id,roomStatus);
        }
        @Test
        @DisplayName("수정할 룸정보가 존재하지 않을시 400 BadRequest")
        public void givenNotEditRoomWhenChangeStatusThen400BadRequest() throws Exception {
            // given
            Long id = 1L;
            RoomStatus roomStatus = RoomStatus.INACTIVE;
            given(roomService.changeStatus(any(),any())).willThrow(new IllegalArgumentException("등록된 룸 정보가 존재하지 않습니다."));
            // when
            mockMvc.perform(
                            patch("/api/room/{id}/change-status",id)
                                    .param("roomStatus",roomStatus.toString())
                    )
                    .andDo(print())
                    .andExpectAll(
                            status().isBadRequest(),
                            jsonPath("$.message").value("등록된 룸 정보가 존재하지 않습니다.")
                    );
            // then
            verify(roomService).changeStatus(id,roomStatus);
        }
    }


    @Nested
    @DisplayName("[룸 정보 삭제][DELETE] /api/room/{id}")
    class removeRoom {
        @Test
        @DisplayName("룸 삭제 성공시 200 OK")
        public void givenSuccessWhenRemoveRoomThen200Ok() throws Exception {
            // given && when
            mockMvc.perform(
                            delete("/api/room/{id}",1L)
                    )
                    .andDo(print())
                    .andExpectAll(
                            status().isOk(),
                            jsonPath("$.message").value("삭제에 성공하였습니다.")
                    );
            // then
            verify(roomService).removeRoom(1L);
        }
    }
}
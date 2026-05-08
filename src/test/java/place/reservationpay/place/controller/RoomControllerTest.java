package place.reservationpay.place.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import place.reservationpay.fixtures.RoomFixtures;
import place.reservationpay.place.dto.AddRoomRequest;
import place.reservationpay.place.dto.RoomDto;
import place.reservationpay.place.service.RoomService;
import tools.jackson.databind.ObjectMapper;

import java.time.LocalTime;

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
}
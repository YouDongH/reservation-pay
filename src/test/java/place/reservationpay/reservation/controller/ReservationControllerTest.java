package place.reservationpay.reservation.controller;

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
import place.reservationpay.fixtures.MemberFixtures;
import place.reservationpay.fixtures.ReservationFixtures;
import place.reservationpay.fixtures.RoomFixtures;
import place.reservationpay.member.domain.Member;
import place.reservationpay.place.domain.Room;
import place.reservationpay.reservation.constant.ReservationStatus;
import place.reservationpay.reservation.dto.AddReservationRequest;
import place.reservationpay.reservation.dto.ReservationDto;
import place.reservationpay.reservation.service.ReservationService;
import tools.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(ReservationController.class)
class ReservationControllerTest {
    @MockitoBean
    ReservationService reservationService;
    @Autowired MockMvc mockMvc;
    @Autowired ObjectMapper objectMapper;

    @Nested
    @DisplayName("[예약하기][POST] /api/reservation")
    class AddReservation {
        @Test
        @DisplayName("예약 성공시 200 OK")
        public void givenSuccessWhenAddReservationThenResponse200OK() throws Exception {
            // given
            AddReservationRequest request = ReservationFixtures.createAddReservationRequest();
            Member member = MemberFixtures.createMember();
            Room room = RoomFixtures.createRoom();
            ReservationDto reservationDto = ReservationFixtures.createReservationDto(member, room);
            given(reservationService.addReservation(any())).willReturn(reservationDto);
            String body = objectMapper.writeValueAsString(request);
            // when
            mockMvc.perform(
                    post("/api/reservation")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(body)
            )
                    .andDo(print())
                    .andExpectAll(
                            status().isOk(),
                            jsonPath("$.message").value("예약이 되었습니다.")
                    );
            // then
            verify(reservationService).addReservation(request);
        }
        @ParameterizedTest
        @CsvSource(value ={
                "startTime is null,nl,4,4,1,1",
                "hours is null,2025-01-25T11:00:00,nl,4,1,1",
                "resCount is null,2025-01-25T11:00:00,4,nl,1,1",
                "memberId is null,2025-01-25T11:00:00,4,4,nl,1",
                "roomId is null,2025-01-25T11:00:00,4,4,1,nl",
                },
                nullValues = "nl"
        )
        @DisplayName("직원정보가 존재하지 않을 경우 성공시 400 BadRequest")
        public void givenNotBodyWhenAddReservationThenResponse400BadRequest(
                String name, LocalDateTime startTime, Integer hours, Integer resCount, Long memberId, Long roomId
        ) throws Exception {
            // given
            AddReservationRequest request = ReservationFixtures.createAddReservationRequest(startTime,hours,resCount,memberId,roomId);
            String body = objectMapper.writeValueAsString(request);
            // when
            mockMvc.perform(
                            post("/api/reservation")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(body)
                    )
                    .andDo(print())
                    .andExpectAll(
                            status().isBadRequest()
                    );
            // then
        }
        @Test
        @DisplayName("직원정보가 존재하지 않을 경우 성공시 400 BadRequest")
        public void givenNotMemberWhenAddReservationThenResponse400BadRequest() throws Exception {
            // given
            AddReservationRequest request = ReservationFixtures.createAddReservationRequest();
            given(reservationService.addReservation(any())).willThrow(new IllegalArgumentException("예약을 진행할 회원이 존재하지않습니다."));
            String body = objectMapper.writeValueAsString(request);
            // when
            mockMvc.perform(
                            post("/api/reservation")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(body)
                    )
                    .andDo(print())
                    .andExpectAll(
                            status().isBadRequest(),
                            jsonPath("$.message").value("예약을 진행할 회원이 존재하지않습니다.")
                    );
            // then
            verify(reservationService).addReservation(request);
        }
        @Test
        @DisplayName("룸정보 조회 실패시 400 BadRequest")
        public void givenNotRoomWhenAddReservationThenResponse400BadRequest() throws Exception {
            // given
            AddReservationRequest request = ReservationFixtures.createAddReservationRequest();
            given(reservationService.addReservation(any())).willThrow(new IllegalArgumentException("예약 대상이 존재하지않습니다."));
            String body = objectMapper.writeValueAsString(request);
            // when
            mockMvc.perform(
                            post("/api/reservation")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(body)
                    )
                    .andDo(print())
                    .andExpectAll(
                            status().isBadRequest(),
                            jsonPath("$.message").value("예약 대상이 존재하지않습니다.")
                    );
            // then
            verify(reservationService).addReservation(request);
        }
    }

    @Nested
    @DisplayName("[예약취소][PATCH] /api/reservation/{id}/cancel")
    class ChangeStatus{
        @Test
        @DisplayName("예약 취소 성공시 200 OK")
        public void givenSuccessWhenChangeStatusThenResponse200OK() throws Exception {
            // given
            given(reservationService.changeStatus(any(),any())).willReturn(1L);
            // when
            mockMvc.perform(
                            patch("/api/reservation/{id}/cancel",1L)
                    )
                    .andDo(print())
                    .andExpectAll(
                            status().isOk(),
                            jsonPath("$.message").value("예약이 취소되었습니다.")
                    );
            // then
            verify(reservationService).changeStatus(1L, ReservationStatus.CANCELLED);
        }
        @Test
        @DisplayName("예약 정보가 존재하지 않을 경우 성공시 400 BadRequest")
        public void givenNotReservationWhenChangeStatusThenResponse400BadRequest() throws Exception {
            // given
            given(reservationService.changeStatus(any(),any()))
                    .willThrow(new IllegalArgumentException("예약정보가 존재하지 않습니다."));
            // when
            mockMvc.perform(
                            patch("/api/reservation/{id}/cancel",1L)
                    )
                    .andDo(print())
                    .andExpectAll(
                            status().isBadRequest(),
                            jsonPath("$.message").value("예약정보가 존재하지 않습니다.")
                    );
            // then
            verify(reservationService).changeStatus(1L, ReservationStatus.CANCELLED);
        }
    }

}
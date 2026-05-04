package place.reservationpay.member.controller;

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
import place.reservationpay.member.constant.Gender;
import place.reservationpay.member.dto.MemberDto;
import place.reservationpay.member.dto.request.AddMemberRequest;
import place.reservationpay.member.dto.request.EditMemberRequest;
import place.reservationpay.member.service.MemberService;
import tools.jackson.databind.ObjectMapper;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MemberApiController.class)
class MemberApiControllerTest {
    @MockitoBean private MemberService memberService;
    @Autowired private ObjectMapper objectMapper;
    @Autowired private MockMvc mockMvc;

    @Nested
    @DisplayName("[POST][회원가입] /api/member")
    class AddMember {
        @Test
        @DisplayName("회원가입 성공시 200 OK")
        public void givenSuccessWhenAddMemberThen200OK() throws Exception {
            // given
            MemberDto dto = MemberFixtures.createMemberDto();
            AddMemberRequest request = MemberFixtures.createAddMemberRequest();
            given(memberService.addMember(any(AddMemberRequest.class))).willReturn(dto);
            String body = objectMapper.writeValueAsString(request);
            // when &&  then
            mockMvc.perform(
                    post("/api/member")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(body)
            )
                    .andDo(print())
                    .andExpectAll(
                            status().isOk()
                    );
            verify(memberService).addMember(request);
        }

        @ParameterizedTest
        @CsvSource(value = {
                "loginId is null,nl,nl,nl,nl,nl,nl",
                "pw is null,nl,nl,nl,nl,nl,nl",
                "birthday is null,nl,nl,nl,nl,nl,nl",
                "gender is null,nl,nl,nl,nl,nl,nl",
                "email is null,nl,nl,nl,nl,nl,nl",
                "mobile is null,nl,nl,nl,nl,nl,nl"
        }, nullValues = "nl")
        @DisplayName("가입 정보 누락시 400 BadRequest")
        public void givenOmittedDataWhenAddMemberThen400BADREQUEST(
                String name, String loginId, String pw, String birthday, Gender gender, String email, String mobile
        ) throws Exception {
            // given
            AddMemberRequest request = MemberFixtures.createAddMemberRequest(loginId, pw, birthday, gender, email, mobile);
            String body = objectMapper.writeValueAsString(request);
            // when && then
            mockMvc.perform(
                    post("/api/member")
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
    @DisplayName("[PATCH][회원정보 수정]")
    class EditMember {
        @Test
        @DisplayName("회원정보 수정시 200 OK")
        public void givenSuccessWhenEditMemberThenResponse200OK() throws Exception {
            // given
            Long id = 1L;
            EditMemberRequest request = MemberFixtures.createEditMemberRequest();
            MemberDto dto = MemberFixtures.createMemberDto();
            given(memberService.editMember(any(),any())).willReturn(dto);
            String body = objectMapper.writeValueAsString(request);
            // when
            mockMvc.perform(
                    patch("/api/member/{id}",id)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(body)
            )
                    .andDo(print())
                    .andExpectAll(
                            status().isOk()
                    );
            // then
            verify(memberService).editMember(id,request);
        }

        @ParameterizedTest
        @CsvSource(value = {
                "email is null,nl,010-1111-2222",
                "mobile is null,test01@naver.com,nl",
        },nullValues = "nl")
        @DisplayName("회원정보 수정값 누락시 400 BadRequest")
        public void givenOmittedDataWhenEditMemberThenResponse400BadRequest(String name, String email, String mobile) throws Exception {
            // given
            Long id = 1L;
            EditMemberRequest request = MemberFixtures.createEditMemberRequest(email,mobile);
            given(memberService.editMember(any(),any())).willThrow(IllegalArgumentException.class);
            String body = objectMapper.writeValueAsString(request);
            // when
            mockMvc.perform(
                            patch("/api/member/{id}",id)
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
        @DisplayName("수정할 직원정보가 존재하지 않을시 400 BadRequest")
        public void givenNotMemberWhenEditMemberThenResponse400BadRequest() throws Exception {
            // given
            Long id = 1L;
            EditMemberRequest request = MemberFixtures.createEditMemberRequest();
            given(memberService.editMember(any(),any())).willThrow(IllegalArgumentException.class);
            String body = objectMapper.writeValueAsString(request);
            // when
            mockMvc.perform(
                            patch("/api/member/{id}",id)
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(body)
                    )
                    .andDo(print())
                    .andExpectAll(
                            status().isBadRequest()
                    );
            // then
            verify(memberService).editMember(id,request);
        }
    }
    @Nested
    @DisplayName("[회원등급 변경][PATCH] /api/member/{id}/change-grade")
    class ChangeGrade {
        @Test
        @DisplayName("회원등급 변경 성공시 200OK 응답")
        public void givenSuccessWhenChangeGradeThenResponse200OK() throws Exception {
            // given
            Long id = 1L;
            String grade = "우수회원";
            given(memberService.changeGrade(any(),any())).willReturn(id);
            // when
            mockMvc.perform(
                    patch("/api/member/{id}/change-grade",id)
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("grade",grade)
            )
                    .andDo(print())
                    .andExpectAll(
                            status().isOk()
                    );
            // then
            verify(memberService).changeGrade(id,grade);
        }

        @Test
        @DisplayName("수정할 회원등급 값 누락시 400 BadRequest")
        public void givenOmittedGradeWhenChangeGradeThenResponse400BadRequest() throws Exception {
            // given
            Long id = 1L;
            // when && then
            mockMvc.perform(
                            patch("/api/member/{id}/change-grade",id)
                                    .contentType(MediaType.APPLICATION_JSON)
                    )
                    .andDo(print())
                    .andExpectAll(
                            status().isBadRequest()
                    );
        }

        @Test
        @DisplayName("수정할 직원정보가 존재하지 않을시 400 BadRequest")
        public void givenNotMemberWhenChangeGradeThenResponse400BadRequest() throws Exception {
            // given
            Long id = 1L;
            String grade = "우수회원";
            given(memberService.changeGrade(any(),any())).willThrow(IllegalArgumentException.class);
            // when
            mockMvc.perform(
                            patch("/api/member/{id}/change-grade",id)
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .param("grade",grade)
                    )
                    .andDo(print())
                    .andExpectAll(
                            status().isBadRequest()
                    );
            // then
            verify(memberService).changeGrade(id,grade);
        }
    }

}
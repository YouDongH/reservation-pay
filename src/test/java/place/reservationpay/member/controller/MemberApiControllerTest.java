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
import place.reservationpay.member.service.MemberService;
import tools.jackson.databind.ObjectMapper;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
}
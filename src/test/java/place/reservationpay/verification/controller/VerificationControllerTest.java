package place.reservationpay.verification.controller;

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
import place.reservationpay.verification.constant.AuthFlag;
import place.reservationpay.verification.dto.CheckVerificationRequest;
import place.reservationpay.verification.dto.SendVerificationRequest;
import place.reservationpay.verification.service.VerificationService;
import tools.jackson.databind.ObjectMapper;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(controllers = VerificationController.class)
class VerificationControllerTest {
    @MockitoBean private VerificationService verificationService;
    @Autowired MockMvc mockMvc;
    @Autowired ObjectMapper objectMapper;

    @Nested
    @DisplayName("[인증메일전송][POST] /api/code/send")
    class SendCode {
        @Test
        @DisplayName("인증메일전송 성공시 200 OK")
        public void test() throws Exception {
            // given
            SendVerificationRequest request = new SendVerificationRequest("test01@naver.com","email");
            String body = objectMapper.writeValueAsString(request);
            // when
            mockMvc.perform(
                    post("/api/code/send")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(body)
            )
                    .andDo(print())
                    .andExpectAll(
                            status().isOk()
                    );
            // then
            verify(verificationService).sendVerificationCode(request);
        }
        @ParameterizedTest
        @CsvSource(value = {"email is null,nl,email","verType is null,test01@naver.com,nl"},nullValues = "nl")
        @DisplayName("입력값이 입력되지않았을경우 400 BadRequest")
        public void tes1t(String name, String email, String verType) throws Exception {
            // given
            SendVerificationRequest request = new SendVerificationRequest(email,verType);
            String body = objectMapper.writeValueAsString(request);
            // when && then
            mockMvc.perform(
                            post("/api/code/send")
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
    @DisplayName("[인증번호 검증][PATCH] /api/code/check")
    class CheckCode {
        @Test
        @DisplayName("인증메일 검증 성공시 200 OK")
        public void givenSuccessWhenCheckCodeThenResponse200OK() throws Exception {
            // given
            CheckVerificationRequest request = new CheckVerificationRequest("test01@naver.com","967586");
            given(verificationService.checkVerification(any())).willReturn(AuthFlag.SUCCESS);
            String body = objectMapper.writeValueAsString(request);
            // when
            mockMvc.perform(
                    patch("/api/code/check")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body)
            )
                    .andDo(print())
                    .andExpectAll(
                            status().isOk()
                    );
            // then
            verify(verificationService).checkVerification(request);
        }

        @ParameterizedTest
        @CsvSource(value = {"email is null,nl,554125","verType is null,test01@naver.com,nl"},nullValues = "nl")
        @DisplayName("입력값이 입력되지않았을경우 400 BadRequest")
        public void givenNotInputParameterWhenCheckCodeThenResponse400BadRequest(String name, String email, String code) throws Exception {
            // given
            SendVerificationRequest request = new SendVerificationRequest(email,code);
            String body = objectMapper.writeValueAsString(request);
            // when && then
            mockMvc.perform(
                            patch("/api/code/check")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(body)
                    )
                    .andDo(print())
                    .andExpectAll(
                            status().isBadRequest()
                    );
        }
        @Test
        @DisplayName("이메일 인증 요청이 없는 경우 409 Conflict")
        public void givenNotSendCodeWhenCheckCodeThenResponse409Conflict() throws Exception {
            // given
            CheckVerificationRequest request = new CheckVerificationRequest("test01@naver.com","967586");
            given(verificationService.checkVerification(any()))
                    .willThrow(new IllegalStateException("요청한 인증이 없습니다."));
            String body = objectMapper.writeValueAsString(request);
            // when
            mockMvc.perform(
                            patch("/api/code/check")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(body)
                    )
                    .andDo(print())
                    .andExpectAll(
                            status().isConflict(),
                            jsonPath("$.message").value("요청한 인증이 없습니다.")
                    );
            // then
            verify(verificationService).checkVerification(request);
        }
        @Test
        @DisplayName("이미 인증한 경우이 409 Conflict")
        public void givenExistVerificationOkWhenCheckCodeThenResponse409Conflict() throws Exception {
            CheckVerificationRequest request = new CheckVerificationRequest("test01@naver.com","967586");
            given(verificationService.checkVerification(any()))
                    .willThrow(new IllegalStateException("이미 인증하였습니다."));
            String body = objectMapper.writeValueAsString(request);
            // when
            mockMvc.perform(
                            patch("/api/code/check")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(body)
                    )
                    .andDo(print())
                    .andExpectAll(
                            status().isConflict(),
                            jsonPath("$.message").value("이미 인증하였습니다.")
                    );
            // then
            verify(verificationService).checkVerification(request);
        }
        @Test
        @DisplayName("인증시간이 경과한 경우 409 Conflict")
        public void givenExpiredTimeWhenCheckCodeThenResponse409Conflict() throws Exception {
            CheckVerificationRequest request = new CheckVerificationRequest("test01@naver.com","967586");
            given(verificationService.checkVerification(any()))
                    .willThrow(new IllegalStateException("인증시간이 경과하였습니다."));
            String body = objectMapper.writeValueAsString(request);
            // when
            mockMvc.perform(
                            patch("/api/code/check")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(body)
                    )
                    .andDo(print())
                    .andExpectAll(
                            status().isConflict(),
                            jsonPath("$.message").value("인증시간이 경과하였습니다.")
                    );
            // then
            verify(verificationService).checkVerification(request);
        }

    }
}
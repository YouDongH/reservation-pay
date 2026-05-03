package place.reservationpay.member.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import place.reservationpay.common.dto.ApiResponse;
import place.reservationpay.member.dto.MemberDto;
import place.reservationpay.member.dto.request.AddMemberRequest;
import place.reservationpay.member.dto.request.EditMemberRequest;
import place.reservationpay.member.service.MemberService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class MemberApiController {
    private final MemberService memberService;

    // 회원등록
    @PostMapping("/member")
    public ApiResponse<MemberDto> addMember(@RequestBody @Valid AddMemberRequest request) {
        MemberDto result = memberService.addMember(request);
        return ApiResponse.success(result, "회원가입에 성공하였습니다.");
    }
    // 회원정보수정
    @PatchMapping("/member/{id}")
    public ApiResponse<MemberDto> editMember(@PathVariable Long id,@RequestBody @Valid EditMemberRequest request) throws Exception {
        MemberDto result = memberService.editMember(id, request);
        return ApiResponse.success(result, "회원정보 수정에 성공하였습니다.");
    }
    // 회원등급변경
    @PatchMapping("/member/{id}/change-grade")
    public ApiResponse<Long> changeGrade(@PathVariable Long id, @NotEmpty String grade) throws Exception {
        return null;
    }
    // 아이디 중복체크
    @GetMapping("/member/check-id")
    public String existId(@NotEmpty String loginId){
        return null;
    }
    // 회원탈퇴
    @DeleteMapping("/member")
    public void removeMember(Long id){

    }

}

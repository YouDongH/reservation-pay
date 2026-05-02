package place.reservationpay.member.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import place.reservationpay.member.domain.Member;
import place.reservationpay.member.dto.MemberDto;
import place.reservationpay.member.dto.request.AddMemberRequest;
import place.reservationpay.member.dto.request.EditMemberRequest;
import place.reservationpay.member.repository.MemberRepository;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    // 직원등록
    public MemberDto addMember(AddMemberRequest request) {
        Member member = Member.createMember(request.loginId(), request.pw(), request.birthday(), request.gender(), request.email(), request.mobile());
        Member result = memberRepository.save(member);
        System.out.println("result = " + result);
        return MemberDto.from(result);
    }

    // 직원정보 수정
    public MemberDto editMember(Long id, EditMemberRequest request) throws Exception {
        Member member = memberRepository.findById(id)
                .orElseThrow(()-> new Exception("조회 결과 존재하지 않습니다."));
        member.updateMember(request.email(), request.mobile());

        return MemberDto.from(member);
    }

    // 직원 등급 변경
    public Long changeGrade(Long id, String grade) throws Exception {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new Exception("조회 결과 존재하지 않습니다."));
        member.changeGrade(grade);

        return member.getId();
    }

    // 아이디 중복 확인
    public String checkLoginId(String loginId) throws Exception {
        boolean result = memberRepository.existId(loginId);
        if(!result){
            return loginId;
        }
        else{
            throw new Exception("이미 등록된 아이디입니다.");
        }
    }

    // 회원탈퇴
    public void removeMember(Long id) throws Exception {
        memberRepository.deleteById(id);
    }

}

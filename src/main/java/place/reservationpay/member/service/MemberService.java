package place.reservationpay.member.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import place.reservationpay.member.domain.Member;
import place.reservationpay.member.dto.MemberDto;
import place.reservationpay.member.dto.request.AddMemberRequest;
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

    // 회원탈퇴
    public void removeMember(Long id) throws Exception {
        memberRepository.deleteById(id);
    }

}

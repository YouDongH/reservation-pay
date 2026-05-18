package place.reservationpay.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import place.reservationpay.member.domain.Member;
import place.reservationpay.member.repository.query.LoginVo;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member,Long> {
    @Query("select exists (select 1 from Member m where m.loginId=:loginId)")
    boolean existId(String loginId);

    @Query("select new  place.reservationpay.member.repository.query.LoginVo(m.id,m.loginId,m.pw,m.grade) from Member m where m.loginId=:loginId")
    LoginVo checkLoginInfo(String loginId);

    @Query("select m.loginId from Member m where m.email=:email and m.name=:name")
    Optional<String> findByEmailAndName(String email, String name);

    @Query("select exists (select 1 from Member m where m.email=:email and m.name=:name and m.loginId=:loginId)")
    boolean findPwCheck(String email, String name, String loginId);
}

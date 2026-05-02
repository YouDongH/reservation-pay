package place.reservationpay.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import place.reservationpay.member.domain.Member;

@Repository
public interface MemberRepository extends JpaRepository<Member,Long> {
    @Query("select exists (select 1 from Member m where m.loginId=:loginId)")
    boolean existId(String loginId);
}

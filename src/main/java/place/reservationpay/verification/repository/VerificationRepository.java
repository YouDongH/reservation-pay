package place.reservationpay.verification.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import place.reservationpay.verification.domain.Verification;


@Repository
public interface VerificationRepository extends JpaRepository<Verification, Long> {
}

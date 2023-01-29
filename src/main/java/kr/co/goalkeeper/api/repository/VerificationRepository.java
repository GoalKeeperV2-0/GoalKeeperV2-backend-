package kr.co.goalkeeper.api.repository;

import kr.co.goalkeeper.api.model.entity.Certification;
import kr.co.goalkeeper.api.model.entity.Verification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VerificationRepository extends JpaRepository<Verification, Long> {
    long countByCertificationAndSuccessTrue(Certification certification);
}

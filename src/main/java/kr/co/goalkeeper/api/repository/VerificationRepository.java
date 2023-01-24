package kr.co.goalkeeper.api.repository;

import kr.co.goalkeeper.api.model.domain.Certification;
import kr.co.goalkeeper.api.model.domain.Verification;
import org.springframework.data.repository.CrudRepository;

public interface VerificationRepository extends CrudRepository<Verification, Long> {
    long countByCertificationAndSuccessTrue(Certification certification);
}

package kr.co.goalkeeper.api.repository;

import kr.co.goalkeeper.api.model.entity.Certification;
import kr.co.goalkeeper.api.model.entity.Verification;
import org.springframework.data.repository.CrudRepository;

public interface VerificationRepository extends CrudRepository<Verification, Long> {
    long countByCertificationAndSuccessTrue(Certification certification);
}

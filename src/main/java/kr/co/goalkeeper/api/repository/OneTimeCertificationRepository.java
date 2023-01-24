package kr.co.goalkeeper.api.repository;

import kr.co.goalkeeper.api.model.domain.OneTimeCertification;
import kr.co.goalkeeper.api.model.domain.OneTimeGoal;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface OneTimeCertificationRepository extends CrudRepository<OneTimeCertification,Long> {
    Optional<OneTimeCertification> findByOneTimeGoal(OneTimeGoal oneTimeGoal);
}

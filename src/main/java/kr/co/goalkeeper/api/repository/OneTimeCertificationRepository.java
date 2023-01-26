package kr.co.goalkeeper.api.repository;

import kr.co.goalkeeper.api.model.entity.OneTimeCertification;
import kr.co.goalkeeper.api.model.entity.OneTimeGoal;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface OneTimeCertificationRepository extends CrudRepository<OneTimeCertification,Long> {
    Optional<OneTimeCertification> findByOneTimeGoal(OneTimeGoal oneTimeGoal);
}

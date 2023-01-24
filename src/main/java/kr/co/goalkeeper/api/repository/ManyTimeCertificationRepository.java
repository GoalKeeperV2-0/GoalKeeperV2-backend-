package kr.co.goalkeeper.api.repository;

import kr.co.goalkeeper.api.model.domain.ManyTimeCertification;
import kr.co.goalkeeper.api.model.domain.ManyTimeGoal;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDate;
import java.util.List;

public interface ManyTimeCertificationRepository extends CrudRepository<ManyTimeCertification,Long> {
    List<ManyTimeCertification> findAllByManyTimeGoal(ManyTimeGoal manyTimeGoal);
    ManyTimeCertification findByManyTimeGoalAndDate(ManyTimeGoal manyTimeGoal, LocalDate date);
}

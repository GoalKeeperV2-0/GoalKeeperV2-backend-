package kr.co.goalkeeper.api.repository;

import kr.co.goalkeeper.api.model.entity.ManyTimeGoalCertDate;
import kr.co.goalkeeper.api.model.entity.ManyTimeGoalCertDateId;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface ManyTimeGoalCertDateRepository extends JpaRepository<ManyTimeGoalCertDate,ManyTimeGoalCertDateId> {
    @EntityGraph(attributePaths = "manyTimeGoal")
    Set<ManyTimeGoalCertDate> findAllByManyTimeGoal_IdIn(Set<Long> goalIds);
}

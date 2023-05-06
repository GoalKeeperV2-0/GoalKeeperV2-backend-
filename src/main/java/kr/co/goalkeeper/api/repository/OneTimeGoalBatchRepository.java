package kr.co.goalkeeper.api.repository;

import kr.co.goalkeeper.api.model.entity.goal.GoalState;
import kr.co.goalkeeper.api.model.entity.goal.OneTimeGoal;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;

public interface OneTimeGoalBatchRepository extends JpaRepository<OneTimeGoal, Long> {
    @EntityGraph(attributePaths = {"user","category"})
    Slice<OneTimeGoal> findAllByGoalStateAndEndDate(GoalState goalState, LocalDate date, Pageable pageable);
}

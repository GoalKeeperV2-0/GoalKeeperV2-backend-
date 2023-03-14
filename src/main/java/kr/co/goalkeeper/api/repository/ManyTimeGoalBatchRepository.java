package kr.co.goalkeeper.api.repository;

import kr.co.goalkeeper.api.model.entity.GoalState;
import kr.co.goalkeeper.api.model.entity.ManyTimeGoal;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;

public interface ManyTimeGoalBatchRepository extends JpaRepository<ManyTimeGoal,Long> {
    Slice<ManyTimeGoal> findAllByGoalStateAndEndDate(GoalState goalState, LocalDate date, Pageable pageable);

}

package kr.co.goalkeeper.api.repository;

import kr.co.goalkeeper.api.model.entity.Goal;
import kr.co.goalkeeper.api.model.entity.GoalState;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;

public interface GoalBatchRepository extends JpaRepository<Goal,Long> {
    @EntityGraph(attributePaths = {"user","category"})
    Slice<Goal> findAllByEndDateAndGoalState(LocalDate date, GoalState goalState, Pageable pageable);
}

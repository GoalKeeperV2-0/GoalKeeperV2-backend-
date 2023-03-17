package kr.co.goalkeeper.api.repository;

import kr.co.goalkeeper.api.model.entity.CategoryType;
import kr.co.goalkeeper.api.model.entity.Goal;
import kr.co.goalkeeper.api.model.entity.GoalState;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GoalRepository extends JpaRepository<Goal,Long> {
    @EntityGraph(attributePaths = "category")
    Page<Goal> findAllByUser_Id(long userId, Pageable pageable);
    @EntityGraph(attributePaths = "category")
    Page<Goal> findAllByUser_IdAndCategory_CategoryType(long userId, CategoryType categoryType, Pageable pageable);
    long countAllByGoalState(GoalState goalState);
    long countAllByUser_Id(long user_id);
    long countAllByGoalStateAndUser_Id(GoalState goalState,long userId);
}

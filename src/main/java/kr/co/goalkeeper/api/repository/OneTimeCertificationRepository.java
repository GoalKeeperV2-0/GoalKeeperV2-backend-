package kr.co.goalkeeper.api.repository;

import kr.co.goalkeeper.api.model.entity.CategoryType;
import kr.co.goalkeeper.api.model.entity.GoalState;
import kr.co.goalkeeper.api.model.entity.OneTimeCertification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OneTimeCertificationRepository extends JpaRepository<OneTimeCertification,Long> {
    Optional<OneTimeCertification> findByGoal_Id(long goalId);
    Page<OneTimeCertification> findByGoal_Category_CategoryTypeAndGoal_GoalState(CategoryType categoryType, GoalState goalState, Pageable pageable);
    Page<OneTimeCertification> findByGoal_GoalState(GoalState goalState, Pageable pageable);
}

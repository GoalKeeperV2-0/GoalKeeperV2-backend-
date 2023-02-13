package kr.co.goalkeeper.api.repository;

import kr.co.goalkeeper.api.model.entity.CategoryType;
import kr.co.goalkeeper.api.model.entity.Certification;
import kr.co.goalkeeper.api.model.entity.GoalState;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CertificationRepository extends JpaRepository<Certification,Long> {
    List<Certification> findAllByGoal_Category_CategoryType(CategoryType categoryType);
    Optional<Certification> findByGoal_Id(long goalId);
    Page<Certification> findByGoal_Category_CategoryTypeAndGoal_GoalState(CategoryType categoryType, GoalState goalState, Pageable pageable);
    Page<Certification> findByGoal_GoalState(GoalState goalState, Pageable pageable);
}

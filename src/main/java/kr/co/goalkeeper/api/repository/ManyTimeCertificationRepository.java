package kr.co.goalkeeper.api.repository;

import kr.co.goalkeeper.api.model.entity.CategoryType;
import kr.co.goalkeeper.api.model.entity.GoalState;
import kr.co.goalkeeper.api.model.entity.ManyTimeCertification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface ManyTimeCertificationRepository extends JpaRepository<ManyTimeCertification,Long> {
    List<ManyTimeCertification> findAllByGoal_Id(long goalId);
    ManyTimeCertification findByGoal_IdAndDate(long goalId, LocalDate date);
    Page<ManyTimeCertification> findByGoal_Category_CategoryTypeAndGoal_GoalState(CategoryType categoryType, GoalState goalState, Pageable pageable);
    Page<ManyTimeCertification> findByGoal_GoalState(GoalState goalState, Pageable pageable);
    Page<ManyTimeCertification> findByGoal_Id(long goalId, Pageable pageable);
}

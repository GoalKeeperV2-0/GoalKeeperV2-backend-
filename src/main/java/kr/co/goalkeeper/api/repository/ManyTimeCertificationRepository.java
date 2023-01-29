package kr.co.goalkeeper.api.repository;

import kr.co.goalkeeper.api.model.entity.CategoryType;
import kr.co.goalkeeper.api.model.entity.ManyTimeCertification;
import kr.co.goalkeeper.api.model.entity.ManyTimeGoal;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface ManyTimeCertificationRepository extends JpaRepository<ManyTimeCertification,Long> {
    List<ManyTimeCertification> findAllByManyTimeGoal(ManyTimeGoal manyTimeGoal);
    ManyTimeCertification findByManyTimeGoalAndDate(ManyTimeGoal manyTimeGoal, LocalDate date);
    Page<ManyTimeCertification> findByManyTimeGoal_Category_CategoryType(CategoryType categoryType, Pageable pageable);
    Page<ManyTimeCertification> findByManyTimeGoal_Id(long goalId,Pageable pageable);
}

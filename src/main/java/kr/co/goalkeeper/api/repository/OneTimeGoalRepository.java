package kr.co.goalkeeper.api.repository;

import kr.co.goalkeeper.api.model.entity.CategoryType;
import kr.co.goalkeeper.api.model.entity.OneTimeGoal;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OneTimeGoalRepository extends JpaRepository<OneTimeGoal,Long> {
    Page<OneTimeGoal> findAllByUser_Id(long userId,Pageable pageable);
    Page<OneTimeGoal> findAllByUser_IdAndCategory_CategoryType(long userId, CategoryType categoryType, Pageable pageable);
}

package kr.co.goalkeeper.api.repository;

import kr.co.goalkeeper.api.model.entity.CategoryType;
import kr.co.goalkeeper.api.model.entity.ManyTimeGoal;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ManyTimeGoalRepository extends JpaRepository<ManyTimeGoal,Long> {
    Page<ManyTimeGoal> findAllByUser_Id(long userId, Pageable pageable);
    Page<ManyTimeGoal> findAllByUser_IdAndCategory_CategoryType(long userId, CategoryType categoryType, Pageable pageable);
}

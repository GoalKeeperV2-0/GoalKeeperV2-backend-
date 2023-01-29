package kr.co.goalkeeper.api.repository;

import kr.co.goalkeeper.api.model.entity.CategoryType;
import kr.co.goalkeeper.api.model.entity.OneTimeCertification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OneTimeCertificationRepository extends JpaRepository<OneTimeCertification,Long> {
    OneTimeCertification findByOneTimeGoal_Id(long goalId);
    Page<OneTimeCertification> findByOneTimeGoal_Category_CategoryType(CategoryType categoryType, Pageable pageable);
}

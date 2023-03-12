package kr.co.goalkeeper.api.repository;

import kr.co.goalkeeper.api.model.entity.CategoryType;
import kr.co.goalkeeper.api.model.entity.Certification;
import kr.co.goalkeeper.api.model.entity.CertificationState;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;

public interface CertificationRepository extends JpaRepository<Certification,Long> {
    Page<Certification> findAllByGoal_Id(long goalId,Pageable pageable);
    boolean existsByDateAndGoal_Id(LocalDate date,long goalId);
    Page<Certification> findByGoal_Category_CategoryTypeAndState(CategoryType categoryType, CertificationState certificationState, Pageable pageable);
    Page<Certification> findByState(CertificationState certificationState, Pageable pageable);
}

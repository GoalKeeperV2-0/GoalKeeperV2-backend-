package kr.co.goalkeeper.api.service.port;

import kr.co.goalkeeper.api.model.entity.CategoryType;
import kr.co.goalkeeper.api.model.entity.Certification;
import org.springframework.data.domain.Page;

public interface CertificationGetService {
    Page<Certification> getCertificationsByGoalId(long goalId,int page);
    Page<Certification> getCertificationsByCategory(CategoryType categoryType, int page);
    Page<Certification> getCertifications(int page);
    Certification getCertificationById(long certificationId);
}

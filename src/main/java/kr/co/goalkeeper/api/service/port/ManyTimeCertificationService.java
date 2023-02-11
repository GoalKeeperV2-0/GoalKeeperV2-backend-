package kr.co.goalkeeper.api.service.port;

import kr.co.goalkeeper.api.model.entity.CategoryType;
import kr.co.goalkeeper.api.model.entity.ManyTimeCertification;
import org.springframework.data.domain.Page;

public interface ManyTimeCertificationService {
    ManyTimeCertification createCertification(ManyTimeCertification certification,long userId);
    Page<ManyTimeCertification> getCertificationsByGoalId(long goalId,int page);
    Page<ManyTimeCertification> getManyTimeCertificationsByCategory(CategoryType categoryType,int page);
    Page<ManyTimeCertification> getManyTimeCertifications(int page);
}

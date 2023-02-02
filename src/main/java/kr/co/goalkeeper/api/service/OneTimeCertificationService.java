package kr.co.goalkeeper.api.service;

import kr.co.goalkeeper.api.model.entity.CategoryType;
import kr.co.goalkeeper.api.model.entity.OneTimeCertification;
import org.springframework.data.domain.Page;

public interface OneTimeCertificationService {
    OneTimeCertification createCertification(OneTimeCertification certification,long userId);
    OneTimeCertification getCertificationByGoalId(long goalId);
    Page<OneTimeCertification> getOneTimeCertificationsByCategory(CategoryType categoryType,int page);
}

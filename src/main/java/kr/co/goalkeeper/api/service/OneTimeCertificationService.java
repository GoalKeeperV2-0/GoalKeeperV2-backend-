package kr.co.goalkeeper.api.service;

import kr.co.goalkeeper.api.model.entity.CategoryType;
import kr.co.goalkeeper.api.model.entity.OneTimeCertification;
import org.springframework.data.domain.Page;

import java.util.List;

public interface OneTimeCertificationService {
    void createCertification(OneTimeCertification certification);
    OneTimeCertification getCertificationByGoalId(long goalId);
    Page<OneTimeCertification> getOneTimeCertificationsByCategory(CategoryType categoryType,int page);
}

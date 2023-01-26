package kr.co.goalkeeper.api.service;

import kr.co.goalkeeper.api.model.domain.CategoryType;
import kr.co.goalkeeper.api.model.domain.OneTimeCertification;

import java.util.List;

public interface OneTimeCertificationService {
    void createCertification(OneTimeCertification certification);
    OneTimeCertification getCertificationByGoalId(long goalId);
    List<OneTimeCertification> getCertificationsByCategory(CategoryType categoryType);
}

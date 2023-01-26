package kr.co.goalkeeper.api.service;

import kr.co.goalkeeper.api.model.domain.CategoryType;
import kr.co.goalkeeper.api.model.domain.ManyTimeCertification;

import java.util.List;

public interface ManyTimeCertificationService {
    void createCertification(ManyTimeCertification certification);
    List<ManyTimeCertification> getCertificationsByGoalId(long goalId);
    List<ManyTimeCertification> getCertificationsByCategory(CategoryType categoryType);
}

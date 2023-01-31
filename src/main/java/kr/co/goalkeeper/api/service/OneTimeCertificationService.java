package kr.co.goalkeeper.api.service;

import kr.co.goalkeeper.api.model.entity.CategoryType;
import kr.co.goalkeeper.api.model.entity.OneTimeCertification;
import kr.co.goalkeeper.api.model.response.OnetimeCertificationResponse;
import org.springframework.data.domain.Page;

import java.util.List;

public interface OneTimeCertificationService {
    OnetimeCertificationResponse createCertification(OneTimeCertification certification);
    OneTimeCertification getCertificationByGoalId(long goalId);
    Page<OneTimeCertification> getOneTimeCertificationsByCategory(CategoryType categoryType,int page);
}

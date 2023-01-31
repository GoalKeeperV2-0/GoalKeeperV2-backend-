package kr.co.goalkeeper.api.service;

import kr.co.goalkeeper.api.model.entity.CategoryType;
import kr.co.goalkeeper.api.model.entity.ManyTimeCertification;
import kr.co.goalkeeper.api.model.response.ManyTimeCertificationResponse;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ManyTimeCertificationService {
    ManyTimeCertificationResponse createCertification(ManyTimeCertification certification);
    Page<ManyTimeCertification> getCertificationsByGoalId(long goalId,int page);
    Page<ManyTimeCertification> getManyTimeCertificationsByCategory(CategoryType categoryType,int page);
}

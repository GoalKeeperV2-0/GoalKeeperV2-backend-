package kr.co.goalkeeper.api.service.port;

import kr.co.goalkeeper.api.model.entity.CategoryType;
import kr.co.goalkeeper.api.model.entity.goal.Certification;
import kr.co.goalkeeper.api.model.response.CertificationPageResponse;

public interface CertificationGetService {
    CertificationPageResponse getCertificationsByGoalId(long goalId,long userId,int page);
    CertificationPageResponse getCertificationsByCategory(CategoryType categoryType, long userId, int page);
    CertificationPageResponse getCertifications(long userId,int page);
    Certification getCertificationById(long certificationId);
}

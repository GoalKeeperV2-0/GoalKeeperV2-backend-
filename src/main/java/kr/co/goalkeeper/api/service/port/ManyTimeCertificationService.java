package kr.co.goalkeeper.api.service.port;

import kr.co.goalkeeper.api.model.entity.goal.ManyTimeCertification;

public interface ManyTimeCertificationService {
    ManyTimeCertification createCertification(ManyTimeCertification certification,long userId);
}

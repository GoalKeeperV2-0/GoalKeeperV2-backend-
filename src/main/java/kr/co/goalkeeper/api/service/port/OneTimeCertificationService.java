package kr.co.goalkeeper.api.service.port;

import kr.co.goalkeeper.api.model.entity.goal.OneTimeCertification;

public interface OneTimeCertificationService {
    OneTimeCertification createCertification(OneTimeCertification certification,long userId);

}

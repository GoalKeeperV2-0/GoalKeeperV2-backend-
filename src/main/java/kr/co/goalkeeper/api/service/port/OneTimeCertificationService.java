package kr.co.goalkeeper.api.service.port;

import kr.co.goalkeeper.api.model.entity.OneTimeCertification;

public interface OneTimeCertificationService {
    OneTimeCertification createCertification(OneTimeCertification certification,long userId);

}

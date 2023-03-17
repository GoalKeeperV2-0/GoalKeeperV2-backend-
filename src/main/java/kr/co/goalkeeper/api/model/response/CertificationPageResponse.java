package kr.co.goalkeeper.api.model.response;

import kr.co.goalkeeper.api.model.entity.Certification;
import kr.co.goalkeeper.api.model.entity.ManyTimeCertification;
import kr.co.goalkeeper.api.model.entity.OneTimeCertification;
import kr.co.goalkeeper.api.model.entity.Verification;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

@Getter
public class CertificationPageResponse {
    private final Function<? super Certification, ? extends CertificationResponse> mapper = certification -> {
        if(certification instanceof ManyTimeCertification){
            return new ManyTimeCertificationResponse((ManyTimeCertification) certification);
        }else{
            return new OneTimeCertificationResponse((OneTimeCertification) certification);
        }
    };
    private Page<CertificationResponse> certificationResponses;
    private final List<Long> alreadyVerification;
    public CertificationPageResponse(Page<Certification> certificationResponses, List<Verification> verifications, Set<Certification> certificationsInGoalIds){
        this.certificationResponses = certificationResponses.map(mapper);
        this.certificationResponses = this.certificationResponses.map(certificationResponse -> {
            if(certificationResponse instanceof ManyTimeCertificationResponse){
                return new ManyTimeCertificationResponse((ManyTimeCertificationResponse) certificationResponse,certificationsInGoalIds);
            }
            return certificationResponse;
        });
        alreadyVerification = new ArrayList<>();
        verifications.forEach(verification -> alreadyVerification.add(verification.getCertification().getId()));
    }
}

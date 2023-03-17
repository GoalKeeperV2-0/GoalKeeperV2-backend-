package kr.co.goalkeeper.api.model.response;

import kr.co.goalkeeper.api.model.entity.Certification;
import kr.co.goalkeeper.api.model.entity.ManyTimeCertification;
import kr.co.goalkeeper.api.model.entity.OneTimeCertification;
import kr.co.goalkeeper.api.model.entity.Verification;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.HashSet;
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
                Set<Certification> sameGoalCerts = new HashSet<>();
                certificationsInGoalIds.forEach(c -> {
                    if(c.getId() == certificationResponse.id){
                        return;
                    }
                    if(c.getGoal().getId() != ((ManyTimeCertificationResponse) certificationResponse).getManyTimeGoal().getId()){
                        return;
                    }
                    sameGoalCerts.add(c);
                });
                return new ManyTimeCertificationResponse((ManyTimeCertificationResponse) certificationResponse,sameGoalCerts);
            }
            return certificationResponse;
        });
        alreadyVerification = new ArrayList<>();
        verifications.forEach(verification -> alreadyVerification.add(verification.getCertification().getId()));
    }
}

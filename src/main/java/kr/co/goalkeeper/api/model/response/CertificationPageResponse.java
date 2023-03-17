package kr.co.goalkeeper.api.model.response;

import kr.co.goalkeeper.api.model.entity.*;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
public class CertificationPageResponse {
    private final Page<CertificationResponse> certificationResponses;
    private final List<Long> alreadyVerification;
    public CertificationPageResponse(Page<Certification> certificationResponses, List<Verification> verifications, Set<Certification> certificationsInGoalIds,Set<ManyTimeGoalCertDate> certDates){
        this.certificationResponses = certificationResponses.map(certification -> {
            if(certification instanceof ManyTimeCertification){
                Set<Certification> sameGoalCerts = new HashSet<>();
                certificationsInGoalIds.forEach(c -> {
                    if(c.getId() == certification.getId()){
                        return;
                    }
                    if(c.getGoal().getId() != certification.getGoal().getId()){
                        return;
                    }
                    sameGoalCerts.add(c);
                });
                return ManyTimeCertificationResponse.getSelectCertificationResponse((ManyTimeCertification) certification,sameGoalCerts,certDates);
            }else {
                return new OneTimeCertificationResponse((OneTimeCertification) certification);
            }
        });
        alreadyVerification = new ArrayList<>();
        verifications.forEach(verification -> alreadyVerification.add(verification.getCertification().getId()));
    }
}

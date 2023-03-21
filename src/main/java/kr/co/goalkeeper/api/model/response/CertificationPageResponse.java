package kr.co.goalkeeper.api.model.response;

import kr.co.goalkeeper.api.model.entity.*;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.*;

@Getter
public class CertificationPageResponse {
    private final Page<CertificationResponse> certificationResponses;
    private final List<Long> alreadyVerification;
    public static final CertificationPageResponse getEmptyResponse(){
        return new CertificationPageResponse();
    }
    private CertificationPageResponse(){
        certificationResponses = Page.empty();
        alreadyVerification = new ArrayList<>();
    }
    public CertificationPageResponse(Page<Certification> certificationResponses, List<Verification> verifications, Set<Certification> certificationsInGoalIds,Set<ManyTimeGoalCertDate> certDates){
        this.certificationResponses = certificationResponses.map(certification -> {
            if(certification instanceof ManyTimeCertification) {
                Set<Certification> sameGoalCerts = sameGoalFiltering(certification,certificationsInGoalIds);
                return ManyTimeCertificationResponse.getSelectCertificationResponse((ManyTimeCertification) certification,sameGoalCerts,certDates);
            }
            if(certification instanceof OneTimeCertification) {
                return new OneTimeCertificationResponse((OneTimeCertification) certification);
            }
            return null;
        });
        alreadyVerification = new ArrayList<>();
        verifications.forEach(verification -> alreadyVerification.add(verification.getCertification().getId()));
    }
    private Set<Certification> sameGoalFiltering(Certification certification,Set<Certification> certifications){
        Set<Certification> result = new HashSet<>();
        for (Certification c : certifications) {
            if (c.getId() == certification.getId()) {
                continue;
            }
            if (c.getGoal().getId() != certification.getGoal().getId()) {
                continue;
            }
            result.add(c);
        }
        return result;
    }
}

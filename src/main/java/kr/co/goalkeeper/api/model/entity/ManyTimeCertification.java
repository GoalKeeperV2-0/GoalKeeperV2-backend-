package kr.co.goalkeeper.api.model.entity;

import kr.co.goalkeeper.api.model.request.ManyTimeCertificationRequest;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.time.LocalDate;

@Entity
@DiscriminatorValue("ManyTimeCertification")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ManyTimeCertification extends Certification {

    public ManyTimeCertification(ManyTimeCertificationRequest dto,Goal goal){
        content = dto.getContent();
        picture = dto.getPicture();
        state = CertificationState.ONGOING;
        date = LocalDate.now();
        this.goal = goal;
    }

    @Override
    public void verificationSuccess() {
        int requiredSuccessCount = goal.requiredSuccessCount();
        successCount++;
        if(successCount>=requiredSuccessCount){
            success();
            ((ManyTimeGoal)goal).successCertification();
        }
    }

    @Override
    public void verificationFail() {
        int requiredSuccessCount = goal.requiredSuccessCount();
        failCount++;
        if(failCount>=Math.round(0.7 * requiredSuccessCount)){
            fail();
            ((ManyTimeGoal)goal).failCertification();
        }
    }
}

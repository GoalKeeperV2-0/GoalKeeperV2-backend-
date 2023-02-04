package kr.co.goalkeeper.api.model.entity;

import kr.co.goalkeeper.api.model.request.ManyTimeCertificationRequest;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@DiscriminatorValue("ManyTimeCertification")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ManyTimeCertification extends Certification {
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "goal_id",nullable = false)
    @NotNull
    @Setter
    private ManyTimeGoal manyTimeGoal;
    public ManyTimeCertification(ManyTimeCertificationRequest dto){
        content = dto.getContent();
        picture = dto.getPicture();
        state = CertificationState.ONGOING;
        date = dto.getDate();
    }

    @Override
    public void verificationSuccess() {
        int requiredSuccessCount = manyTimeGoal.requiredSuccessCount();
        successCount++;
        if(successCount>=requiredSuccessCount){
            success();
            manyTimeGoal.successCertification();
        }
    }

    @Override
    public void verificationFail() {
        int requiredSuccessCount = manyTimeGoal.requiredSuccessCount();
        failCount++;
        if(failCount>=Math.round(0.7 * requiredSuccessCount)){
            hold();
        }
    }
}

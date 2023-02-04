package kr.co.goalkeeper.api.model.entity;

import kr.co.goalkeeper.api.model.request.OnetimeCertificationRequest;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@DiscriminatorValue("OneTimeCertification")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OneTimeCertification extends Certification {
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="goal_id",nullable = false)
    @Setter
    @NotNull
    private OneTimeGoal oneTimeGoal;

    public OneTimeCertification(OnetimeCertificationRequest dto){
        content = dto.getContent();
        picture = dto.getPicture();
        state = CertificationState.ONGOING;
        date = dto.getDate();
    }
    @Override
    public void verificationSuccess() {
        int requiredSuccessCount = oneTimeGoal.requiredSuccessCount();
        successCount++;
        if(successCount>=requiredSuccessCount){
            success();
        }
    }

    @Override
    public void verificationFail() {
        int requiredSuccessCount = oneTimeGoal.requiredSuccessCount();
        failCount++;
        if(failCount>=Math.round(0.7 * requiredSuccessCount)){
            hold();
        }
    }
    @Override
    protected void success() {
        super.success();
        oneTimeGoal.success();
    }

    @Override
    protected void hold() {
        super.hold();
        oneTimeGoal.hold();
    }
}

package kr.co.goalkeeper.api.model.entity;

import kr.co.goalkeeper.api.model.request.OnetimeCertificationRequest;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;

@Entity
@DiscriminatorValue("OneTimeCertification")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OneTimeCertification extends Certification {
    @OneToOne
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
}

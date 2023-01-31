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

@Entity
@DiscriminatorValue("OneTimeCertification")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OneTimeCertification extends Certification {
    @OneToOne
    @JoinColumn(name="goal_id")
    @Setter
    private OneTimeGoal oneTimeGoal;

    public OneTimeCertification(OnetimeCertificationRequest dto){
        content = dto.getContent();
        picture = dto.getPicture();
        state = dto.getState();
        date = dto.getDate();
    }
}

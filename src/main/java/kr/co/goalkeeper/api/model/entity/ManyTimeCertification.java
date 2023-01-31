package kr.co.goalkeeper.api.model.entity;

import kr.co.goalkeeper.api.model.request.ManyTimeCertificationRequest;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
@DiscriminatorValue("ManyTimeCertification")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ManyTimeCertification extends Certification {
    @ManyToOne
    @JoinColumn(name = "goal_id")
    @Setter
    private ManyTimeGoal manyTimeGoal;
    public ManyTimeCertification(ManyTimeCertificationRequest dto){
        content = dto.getContent();
        picture = dto.getPicture();
        state = dto.getState();
        date = dto.getDate();
    }
}

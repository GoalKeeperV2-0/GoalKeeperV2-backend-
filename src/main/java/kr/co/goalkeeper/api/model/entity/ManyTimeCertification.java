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
}

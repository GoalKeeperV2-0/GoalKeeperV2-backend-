package kr.co.goalkeeper.api.model.domain;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

@Entity
@DiscriminatorValue("OneTimeCertification")
public class OneTimeCertification extends Certification {
    @OneToOne
    @JoinColumn(name="goal_id")
    private OneTimeGoal oneTimeGoal;
}

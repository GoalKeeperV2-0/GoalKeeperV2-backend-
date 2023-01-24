package kr.co.goalkeeper.api.model.domain;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.time.LocalDate;

@Entity
@DiscriminatorValue("ManyTimeCertification")
public class ManyTimeCertification extends Certification {
    @ManyToOne
    @JoinColumn(name = "goal_id")
    private ManyTimeGoal manyTimeGoal;
}

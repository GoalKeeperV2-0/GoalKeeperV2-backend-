package kr.co.goalkeeper.api.model.entity;

import lombok.Getter;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

@Entity
@DiscriminatorValue("OneTimeCertification")
@Getter
public class OneTimeCertification extends Certification {
    @OneToOne
    @JoinColumn(name="goal_id")
    private OneTimeGoal oneTimeGoal;
}

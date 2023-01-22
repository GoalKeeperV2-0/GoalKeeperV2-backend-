package kr.co.goalkeeper.api.model.domain;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Entity
@DiscriminatorValue("OneTimeGoal")
public class OneTimeGoal extends Goal {
    @Column
    @NotNull
    private LocalDate endDate;
}

package kr.co.goalkeeper.api.model.entity.goal;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.time.LocalDate;
@Embeddable
@Getter
@Setter
@NoArgsConstructor
public class ManyTimeGoalCertDateId implements Serializable {
    @Column
    private long goalId;
    @Column
    private LocalDate certDate;
}

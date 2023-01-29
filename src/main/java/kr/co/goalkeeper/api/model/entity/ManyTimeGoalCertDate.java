package kr.co.goalkeeper.api.model.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ManyTimeGoalCertDate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @ManyToOne
    @JoinColumn(name = "goal_id")
    private ManyTimeGoal manyTimeGoal;
    @Column
    @NotNull
    private LocalDate certDate;
    @Builder
    private ManyTimeGoalCertDate(long id, ManyTimeGoal manyTimeGoal, LocalDate certDate) {
        this.id = id;
        this.manyTimeGoal = manyTimeGoal;
        this.certDate = certDate;
    }
}

package kr.co.goalkeeper.api.model.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ManyTimeGoalCertDate {
    @EmbeddedId
    private ManyTimeGoalCertDateId id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "goal_id")
    @MapsId("goalId")
    private ManyTimeGoal manyTimeGoal;
    @Builder
    private ManyTimeGoalCertDate(ManyTimeGoal manyTimeGoal, LocalDate certDate) {
        id = new ManyTimeGoalCertDateId();
        this.manyTimeGoal = manyTimeGoal;
        id.setCertDate(certDate);
    }
    public LocalDate getCertDate(){
        return id.getCertDate();
    }
}

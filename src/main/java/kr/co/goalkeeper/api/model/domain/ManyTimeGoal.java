package kr.co.goalkeeper.api.model.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Entity
@DiscriminatorValue("ManyTimeGoal")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ManyTimeGoal extends Goal {
    @Column
    @NotNull
    private LocalDate startDate;
    @Column
    @NotNull
    private LocalDate endDate;

    @Builder
    private ManyTimeGoal(long id, User user, String title, String content, int point, GoalState goalState, Reward reward, Category category, LocalDate startDate, LocalDate endDate) {
        super(id, user, title, content, point, goalState, reward, category);
        this.startDate = startDate;
        this.endDate = endDate;
    }
}

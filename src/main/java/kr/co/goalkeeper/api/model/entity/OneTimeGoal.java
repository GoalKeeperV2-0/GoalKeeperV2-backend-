package kr.co.goalkeeper.api.model.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Entity
@DiscriminatorValue("OneTimeGoal")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class OneTimeGoal extends Goal {
    @Column
    @NotNull
    private LocalDate endDate;
    @Builder
    private OneTimeGoal(long id, User user, String title, String content, int point, GoalState goalState, Reward reward, Category category, LocalDate endDate) {
        super(id, user, title, content, point, goalState, reward, category);
        this.endDate = endDate;
    }
}

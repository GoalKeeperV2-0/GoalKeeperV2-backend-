package kr.co.goalkeeper.api.model.entity;

import kr.co.goalkeeper.api.model.request.OneTimeGoalRequest;
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
    @Builder
    private OneTimeGoal(long id, User user, String title, String content, int point, GoalState goalState, Reward reward, Category category) {
        super(id, user, title, content, point, goalState, reward, category);
    }

    public OneTimeGoal(OneTimeGoalRequest request, User user){
        this.endDate = request.getEndDate();
        this.goalState = GoalState.ONGOING;
        this.category = new Category(request.getCategoryType());
        this.point = request.getPoint();
        this.content = request.getContent();
        this.title = request.getTitle();
        this.reward = request.getReward();
        this.user = user;
        this.startDate = request.getStartDate();
        this.endDate = request.getEndDate();
        minusUserPoint();
    }
}

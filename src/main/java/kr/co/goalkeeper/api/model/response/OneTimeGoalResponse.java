package kr.co.goalkeeper.api.model.response;


import kr.co.goalkeeper.api.model.entity.OneTimeGoal;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
@SuperBuilder
@Getter
public class OneTimeGoalResponse extends GoalResponse {
    private LocalDate endDate;
    public OneTimeGoalResponse(OneTimeGoal entity){
        this.id =entity.getId();
        this.endDate = entity.getEndDate();
        this.categoryType = entity.getCategory().getCategoryType();
        this.content = entity.getContent();
        this.point = entity.getPoint();
        this.reward = entity.getReward();
        this.title = entity.getTitle();
    }
}

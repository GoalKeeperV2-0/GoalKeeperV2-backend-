package kr.co.goalkeeper.api.model.response;


import kr.co.goalkeeper.api.model.entity.OneTimeGoal;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Getter
public class OneTimeGoalResponse extends GoalResponse {
    public OneTimeGoalResponse(OneTimeGoal entity){
        this.id =entity.getId();
        this.startDate = entity.getStartDate();
        this.endDate = entity.getEndDate();
        this.categoryType = entity.getCategory().getCategoryType();
        this.content = entity.getContent();
        this.point = entity.getPoint();
        this.reward = entity.getReward();
        this.title = entity.getTitle();
    }
}

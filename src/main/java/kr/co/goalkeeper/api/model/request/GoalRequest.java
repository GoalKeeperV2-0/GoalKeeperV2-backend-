package kr.co.goalkeeper.api.model.request;

import kr.co.goalkeeper.api.model.entity.CategoryType;
import kr.co.goalkeeper.api.model.entity.Reward;
import lombok.Getter;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
@Getter
public abstract class GoalRequest {
    protected String title;
    protected CategoryType categoryType;
    protected String content;
    @Min(0)
    @Max(10000)
    protected int point;
    protected Reward reward;
}

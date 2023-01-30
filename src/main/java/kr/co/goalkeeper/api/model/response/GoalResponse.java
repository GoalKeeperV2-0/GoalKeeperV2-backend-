package kr.co.goalkeeper.api.model.response;

import kr.co.goalkeeper.api.model.entity.CategoryType;
import kr.co.goalkeeper.api.model.entity.Reward;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
@SuperBuilder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class GoalResponse {
    protected long id;
    protected String title;
    protected CategoryType categoryType;
    protected String content;
    @Min(0)
    @Max(10000)
    protected int point;
    protected Reward reward;
}

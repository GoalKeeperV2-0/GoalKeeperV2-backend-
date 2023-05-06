package kr.co.goalkeeper.api.model.request;

import kr.co.goalkeeper.api.model.entity.CategoryType;
import kr.co.goalkeeper.api.model.entity.goal.Reward;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.time.LocalDate;

@Getter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PACKAGE)
public abstract class GoalRequest {
    protected String title;
    protected CategoryType categoryType;
    protected String content;
    @Min(0)
    @Max(10000)
    protected int point;
    protected Reward reward;
    protected LocalDate endDate;
}

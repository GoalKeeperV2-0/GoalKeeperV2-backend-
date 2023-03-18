package kr.co.goalkeeper.api.model.request;

import kr.co.goalkeeper.api.model.entity.CategoryType;
import kr.co.goalkeeper.api.model.entity.Reward;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.time.LocalDate;

@Getter
@NoArgsConstructor
public class OneTimeGoalRequest extends GoalRequest {
    public static OneTimeGoalRequest getTestInstance(CategoryType categoryType, @Min(0) @Max(10000) int point, Reward reward, LocalDate endDate) {
        String title = "title";
        String content = "content";
        return new OneTimeGoalRequest(title, categoryType, content, point, reward, endDate);
    }
    public static OneTimeGoalRequest getTestInstance(String title,String content,CategoryType categoryType, @Min(0) @Max(10000) int point, Reward reward, LocalDate endDate) {
        return new OneTimeGoalRequest(title, categoryType, content, point, reward, endDate);
    }
    private OneTimeGoalRequest(String title, CategoryType categoryType, String content, @Min(0) @Max(10000) int point, Reward reward, LocalDate endDate) {
        super(title, categoryType, content, point, reward, endDate);
    }
}

package kr.co.goalkeeper.api.model.request;

import kr.co.goalkeeper.api.model.entity.CategoryType;
import kr.co.goalkeeper.api.model.entity.goal.Reward;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;
@Getter
@NoArgsConstructor
public class ManyTimeGoalRequest extends GoalRequest {
    private List<LocalDate> certDates;
    public static ManyTimeGoalRequest getTestInstance(CategoryType categoryType, int point, Reward reward, LocalDate endDate, List<LocalDate> certDates) {
        String title = "title";
        String content = "content";
        return new ManyTimeGoalRequest(title,categoryType,content,point,reward,endDate,certDates);
    }
    public static ManyTimeGoalRequest getTestInstance(String title,String content,CategoryType categoryType, int point, Reward reward, LocalDate endDate, List<LocalDate> certDates) {
        return new ManyTimeGoalRequest(title,categoryType,content,point,reward,endDate,certDates);
    }
    private ManyTimeGoalRequest(String title, CategoryType categoryType, String content, int point, Reward reward, LocalDate endDate, List<LocalDate> certDates) {
        super(title, categoryType, content, point, reward, endDate);
        this.certDates = certDates;
    }
}

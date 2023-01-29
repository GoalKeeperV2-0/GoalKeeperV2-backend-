package kr.co.goalkeeper.api.model.response;

import kr.co.goalkeeper.api.model.entity.ManyTimeGoal;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.util.List;
@SuperBuilder
@Getter
public class ManyTimeGoalResponse extends GoalResponse {
    private LocalDate startDate;
    private LocalDate endDate;
    private List<LocalDate> certDates;
}

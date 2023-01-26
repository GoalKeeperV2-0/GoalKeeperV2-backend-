package kr.co.goalkeeper.api.model.request;

import lombok.Getter;

import java.time.LocalDate;
import java.util.List;
@Getter
public class ManyTimeGoalRequest extends GoalRequest {
    private LocalDate startDate;
    private LocalDate endDate;
    private List<LocalDate> certDates;
}

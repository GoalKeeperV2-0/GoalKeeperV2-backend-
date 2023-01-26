package kr.co.goalkeeper.api.model.response;


import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
@SuperBuilder
@Getter
public class OneTimeGoalResponse extends GoalResponse {
    private LocalDate endDate;
}

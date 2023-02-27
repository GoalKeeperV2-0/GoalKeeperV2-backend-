package kr.co.goalkeeper.api.model.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class Statistics {
    private long totalGoalCount;
    private long totalSuccessGoalCount;
    private long totalFailGoalCount;
    private long totalOngoingGoalCount;
}

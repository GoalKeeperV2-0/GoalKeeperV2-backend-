package kr.co.goalkeeper.api.model.response;

import kr.co.goalkeeper.api.model.entity.ManyTimeGoal;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
@SuperBuilder
@Getter
public class ManyTimeGoalResponse extends GoalResponse {
    private LocalDate startDate;
    private LocalDate endDate;
    private List<LocalDate> certDates;
    public ManyTimeGoalResponse(ManyTimeGoal entity){
        id = entity.getId();
        title = entity.getTitle();
        content = entity.getContent();
        categoryType = entity.getCategory().getCategoryType();
        point = entity.getPoint();
        reward = entity.getReward();
        startDate = entity.getStartDate();
        endDate = entity.getEndDate();
        certDates = new ArrayList<>();
        entity.getCertDates().forEach((c)-> certDates.add(c.getCertDate()));
        Collections.sort(certDates);
    }
}

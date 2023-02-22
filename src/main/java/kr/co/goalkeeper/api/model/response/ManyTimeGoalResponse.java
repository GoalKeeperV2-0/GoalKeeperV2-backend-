package kr.co.goalkeeper.api.model.response;

import kr.co.goalkeeper.api.model.entity.Certification;
import kr.co.goalkeeper.api.model.entity.ManyTimeCertification;
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
        goalState = entity.getGoalState();
        holdable = entity.isHoldRequestAble();
        certDates = new ArrayList<>();
        entity.getCertDates().forEach((c)-> certDates.add(c.getCertDate()));
        Collections.sort(certDates);
        certifications = new ArrayList<>();
        try {
            for (Certification manyTimeCertification: entity.getCertificationList()) {
                certifications.add(ManyTimeCertificationResponse.makeInstanceWithOutGoal((ManyTimeCertification) manyTimeCertification));
            }
        }catch (NullPointerException e){
            certifications = Collections.emptyList();
        }
    }
    public static ManyTimeGoalResponse makeInstanceWithOutCertifications(ManyTimeGoal entity){
        ManyTimeGoalResponse manyTimeGoalResponse = new ManyTimeGoalResponse(entity);
        manyTimeGoalResponse.certifications = null;
        return manyTimeGoalResponse;
    }
}

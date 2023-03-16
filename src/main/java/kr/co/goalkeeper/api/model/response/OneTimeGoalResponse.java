package kr.co.goalkeeper.api.model.response;


import kr.co.goalkeeper.api.model.entity.Certification;
import kr.co.goalkeeper.api.model.entity.OneTimeCertification;
import kr.co.goalkeeper.api.model.entity.OneTimeGoal;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.Collections;

@SuperBuilder
@Getter
@NoArgsConstructor
public class OneTimeGoalResponse extends GoalResponse {
    public OneTimeGoalResponse(OneTimeGoal entity){
        id =entity.getId();
        startDate = entity.getStartDate();
        endDate = entity.getEndDate();
        categoryType = entity.getCategory().getCategoryType();
        content = entity.getContent();
        point = entity.getPoint();
        reward = entity.getReward();
        title = entity.getTitle();
        goalState = entity.getGoalState();
        holdable = entity.isHoldRequestAble();
        certifications = new ArrayList<>();
        try {
            for (Certification certification : entity.getCertificationList()) {
                certifications.add(OneTimeCertificationResponse.makeInstanceWithOutGoal((OneTimeCertification) certification));
            }
        }catch (NullPointerException e){
            certifications = Collections.emptyList();
        }
    }
    public static OneTimeGoalResponse makeInstanceWithOutCertifications(OneTimeGoal entity){
        OneTimeGoalResponse oneTimeGoalResponse = new OneTimeGoalResponse();
        oneTimeGoalResponse.id =entity.getId();
        oneTimeGoalResponse.startDate = entity.getStartDate();
        oneTimeGoalResponse.endDate = entity.getEndDate();
        oneTimeGoalResponse.categoryType = entity.getCategory().getCategoryType();
        oneTimeGoalResponse.content = entity.getContent();
        oneTimeGoalResponse.point = entity.getPoint();
        oneTimeGoalResponse.reward = entity.getReward();
        oneTimeGoalResponse.title = entity.getTitle();
        oneTimeGoalResponse.goalState = entity.getGoalState();
        oneTimeGoalResponse.holdable = entity.isHoldRequestAble();
        oneTimeGoalResponse.certifications = null;
        return oneTimeGoalResponse;
    }
}

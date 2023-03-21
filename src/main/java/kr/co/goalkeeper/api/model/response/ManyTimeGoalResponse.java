package kr.co.goalkeeper.api.model.response;

import kr.co.goalkeeper.api.model.entity.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

@SuperBuilder
@Getter
@NoArgsConstructor
public class ManyTimeGoalResponse extends GoalResponse {
    private List<LocalDate> certDates;
    public static ManyTimeGoalResponse createResponseFromEntity(ManyTimeGoal entity){
        return new ManyTimeGoalResponse(entity);
    }
    private ManyTimeGoalResponse(ManyTimeGoal entity){
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
                certifications.add(ManyTimeCertificationResponse.getInnerCertificationResponse((ManyTimeCertification) manyTimeCertification));
            }
        }catch (NullPointerException e){
            certifications = Collections.emptyList();
        }
    }
    public static ManyTimeGoalResponse getInnerGoalResponse(ManyTimeGoal entity){
        ManyTimeGoalResponse manyTimeGoalResponse = createdByEntity(entity);
        entity.getCertDates().forEach((c)-> manyTimeGoalResponse.certDates.add(c.getCertDate()));
        Collections.sort(manyTimeGoalResponse.certDates);
        manyTimeGoalResponse.certifications = null;
        return manyTimeGoalResponse;
    }
    private static ManyTimeGoalResponse createdByEntity(ManyTimeGoal entity){
        ManyTimeGoalResponse manyTimeGoalResponse = new ManyTimeGoalResponse();
        manyTimeGoalResponse.id = entity.getId();
        manyTimeGoalResponse.title = entity.getTitle();
        manyTimeGoalResponse.content = entity.getContent();
        manyTimeGoalResponse.categoryType = entity.getCategory().getCategoryType();
        manyTimeGoalResponse.point = entity.getPoint();
        manyTimeGoalResponse.reward = entity.getReward();
        manyTimeGoalResponse.startDate = entity.getStartDate();
        manyTimeGoalResponse.endDate = entity.getEndDate();
        manyTimeGoalResponse.goalState = entity.getGoalState();
        manyTimeGoalResponse.holdable = entity.isHoldRequestAble();
        manyTimeGoalResponse.certDates = new ArrayList<>();
        return manyTimeGoalResponse;
    }
    public static ManyTimeGoalResponse getInnerGoalResponse(ManyTimeGoal entity, Set<ManyTimeGoalCertDate> certDates){
        ManyTimeGoalResponse manyTimeGoalResponse = createdByEntity(entity);
        certDates.forEach((c)-> {
            if(c.getManyTimeGoal().getId()!=manyTimeGoalResponse.id){
                return;
            }
            manyTimeGoalResponse.certDates.add(c.getCertDate());
        });
        Collections.sort(manyTimeGoalResponse.certDates);
        manyTimeGoalResponse.certifications = null;
        return manyTimeGoalResponse;
    }
}

package kr.co.goalkeeper.api.model.response;

import kr.co.goalkeeper.api.model.entity.CertificationState;
import kr.co.goalkeeper.api.model.entity.ManyTimeCertification;
import kr.co.goalkeeper.api.model.entity.ManyTimeGoal;
import lombok.Getter;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.time.LocalDate;


@Getter
public class ManyTimeCertificationResponse extends CertificationResponse {
    private ManyTimeGoalResponse manyTimeGoal;
    public ManyTimeCertificationResponse(ManyTimeCertification entity){
        id = entity.getId();
        content = entity.getContent();
        picture = entity.getPicture();
        state = entity.getState();
        date = entity.getDate();
        ManyTimeGoal manyTimeGoal = entity.getManyTimeGoal();
        this.manyTimeGoal = ManyTimeGoalResponse.builder()
                .id(manyTimeGoal.getId())
                .title(manyTimeGoal.getTitle())
                .categoryType(manyTimeGoal.getCategory().getCategoryType())
                .content(manyTimeGoal.getContent())
                .point(manyTimeGoal.getPoint())
                .reward(manyTimeGoal.getReward())
                .startDate(manyTimeGoal.getStartDate())
                .endDate(manyTimeGoal.getEndDate())
                .build();
    }
}

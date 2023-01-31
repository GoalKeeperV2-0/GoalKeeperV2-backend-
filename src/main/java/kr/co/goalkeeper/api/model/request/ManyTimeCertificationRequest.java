package kr.co.goalkeeper.api.model.request;

import kr.co.goalkeeper.api.model.entity.ManyTimeCertification;
import kr.co.goalkeeper.api.model.response.ManyTimeGoalResponse;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor
public class ManyTimeCertificationRequest extends CertificationRequest {
    private long goalId;
    public ManyTimeCertificationRequest(ManyTimeCertification entity){
        content = entity.getContent();
        picture = entity.getPicture();
        state = entity.getState();
        date = entity.getDate();
        goalId = entity.getManyTimeGoal().getId();
    }
}

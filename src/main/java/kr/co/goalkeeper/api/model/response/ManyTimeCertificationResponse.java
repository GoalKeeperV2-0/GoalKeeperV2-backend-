package kr.co.goalkeeper.api.model.response;

import kr.co.goalkeeper.api.model.entity.ManyTimeCertification;
import lombok.Getter;


@Getter
public class ManyTimeCertificationResponse extends CertificationResponse {
    private ManyTimeGoalResponse manyTimeGoal;
    public ManyTimeCertificationResponse(ManyTimeCertification entity){
        id = entity.getId();
        content = entity.getContent();
        picture = entity.getPicture();
        state = entity.getState();
        date = entity.getDate();
        this.manyTimeGoal = new ManyTimeGoalResponse(entity.getManyTimeGoal());
    }
}

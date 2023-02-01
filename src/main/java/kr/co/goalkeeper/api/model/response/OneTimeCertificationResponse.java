package kr.co.goalkeeper.api.model.response;

import kr.co.goalkeeper.api.model.entity.OneTimeCertification;
import lombok.Getter;


@Getter
public class OneTimeCertificationResponse extends CertificationResponse{

    private final OneTimeGoalResponse oneTimeGoal;
    public OneTimeCertificationResponse(OneTimeCertification entity){
        id = entity.getId();
        content = entity.getContent();
        picture = entity.getPicture();
        state = entity.getState();
        date = entity.getDate();
        this.oneTimeGoal = new OneTimeGoalResponse(entity.getOneTimeGoal());
    }
}

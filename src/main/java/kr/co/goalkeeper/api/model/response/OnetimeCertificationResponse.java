package kr.co.goalkeeper.api.model.response;

import kr.co.goalkeeper.api.model.entity.OneTimeCertification;
import kr.co.goalkeeper.api.model.entity.OneTimeGoal;
import lombok.Getter;


@Getter
public class OnetimeCertificationResponse extends CertificationResponse{

    private OneTimeGoalResponse oneTimeGoal;
    public OnetimeCertificationResponse(OneTimeCertification entity){
        id = entity.getId();
        content = entity.getContent();
        picture = entity.getPicture();
        state = entity.getState();
        date = entity.getDate();
        this.oneTimeGoal = new OneTimeGoalResponse(entity.getOneTimeGoal());
    }
}

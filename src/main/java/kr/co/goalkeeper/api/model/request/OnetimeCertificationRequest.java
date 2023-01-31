package kr.co.goalkeeper.api.model.request;

import kr.co.goalkeeper.api.model.entity.OneTimeCertification;
import kr.co.goalkeeper.api.model.response.OneTimeGoalResponse;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor
public class OnetimeCertificationRequest extends CertificationRequest {

    private long goalId;
    public OnetimeCertificationRequest(OneTimeCertification entity){
        content = entity.getContent();
        picture = entity.getPicture();
        state = entity.getState();
        date = entity.getDate();
        goalId = entity.getOneTimeGoal().getId();
    }
}

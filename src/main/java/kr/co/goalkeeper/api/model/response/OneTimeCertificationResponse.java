package kr.co.goalkeeper.api.model.response;

import kr.co.goalkeeper.api.model.entity.OneTimeCertification;
import kr.co.goalkeeper.api.model.entity.OneTimeGoal;
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
        this.oneTimeGoal = OneTimeGoalResponse.makeInstanceWithOutCertifications((OneTimeGoal) entity.getGoal());
        failCount = entity.getFailCount();
        successCount = entity.getSuccessCount();
    }
    public static OneTimeCertificationResponse makeInstanceWithOutGoal(OneTimeCertification entity){
        return new OneTimeCertificationResponse(entity,false);
    }
    private OneTimeCertificationResponse(OneTimeCertification entity,boolean ignore){
        id = entity.getId();
        content = entity.getContent();
        picture = entity.getPicture();
        state = entity.getState();
        date = entity.getDate();
        this.oneTimeGoal = null;
        failCount = entity.getFailCount();
        successCount = entity.getSuccessCount();
    }
}

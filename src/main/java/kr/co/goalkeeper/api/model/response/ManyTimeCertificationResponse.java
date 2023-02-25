package kr.co.goalkeeper.api.model.response;

import kr.co.goalkeeper.api.model.entity.ManyTimeCertification;
import kr.co.goalkeeper.api.model.entity.ManyTimeGoal;
import lombok.Getter;


@Getter
public class ManyTimeCertificationResponse extends CertificationResponse {
    private final ManyTimeGoalResponse manyTimeGoal;
    public ManyTimeCertificationResponse(ManyTimeCertification entity){
        id = entity.getId();
        content = entity.getContent();
        picture = "/api/image/certification/"+entity.getId();
        state = entity.getState();
        date = entity.getDate();
        this.manyTimeGoal = ManyTimeGoalResponse.makeInstanceWithOutCertifications((ManyTimeGoal) entity.getGoal());
        failCount = entity.getFailCount();
        successCount = entity.getSuccessCount();
    }
    public static ManyTimeCertificationResponse makeInstanceWithOutGoal(ManyTimeCertification entity){
        return new ManyTimeCertificationResponse(entity,true);
    }
    private ManyTimeCertificationResponse(ManyTimeCertification entity,boolean ignore){
        id = entity.getId();
        content = entity.getContent();
        picture = "/api/image/certification/"+entity.getId();
        state = entity.getState();
        date = entity.getDate();
        this.manyTimeGoal = null;
        failCount = entity.getFailCount();
        successCount = entity.getSuccessCount();
    }
}

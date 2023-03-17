package kr.co.goalkeeper.api.model.response;

import kr.co.goalkeeper.api.model.entity.Certification;
import kr.co.goalkeeper.api.model.entity.ManyTimeCertification;
import kr.co.goalkeeper.api.model.entity.ManyTimeGoal;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Set;


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
        relatedCertifications = new ArrayList<>();
    }
    public ManyTimeCertificationResponse(ManyTimeCertificationResponse withOutRelated, Set<Certification> relatedEntities){
        id = withOutRelated.id;
        content = withOutRelated.content;
        picture = withOutRelated.picture;
        state = withOutRelated.state;
        date = withOutRelated.date;
        manyTimeGoal = withOutRelated.manyTimeGoal;
        failCount = withOutRelated.failCount;
        successCount = withOutRelated.successCount;
        relatedCertifications = new ArrayList<>();
        relatedEntities.forEach(c -> relatedCertifications.add(new RelatedCertificationResponse(c.getId(),c.getDate(),c.getState())));
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

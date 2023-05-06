package kr.co.goalkeeper.api.model.response;

import kr.co.goalkeeper.api.model.entity.goal.Certification;
import kr.co.goalkeeper.api.model.entity.goal.ManyTimeCertification;
import kr.co.goalkeeper.api.model.entity.goal.ManyTimeGoal;
import kr.co.goalkeeper.api.model.entity.goal.ManyTimeGoalCertDate;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Set;


@Getter
public class ManyTimeCertificationResponse extends CertificationResponse {
    private final ManyTimeGoalResponse manyTimeGoal;
    public static ManyTimeCertificationResponse getCreateCertificationResponse(ManyTimeCertification entity){
        return new ManyTimeCertificationResponse(entity);
    }
    private ManyTimeCertificationResponse(ManyTimeCertification entity){
        id = entity.getId();
        content = entity.getContent();
        picture = "/api/image/certification/"+entity.getId();
        state = entity.getState();
        date = entity.getDate();
        manyTimeGoal = ManyTimeGoalResponse.getInnerGoalResponse((ManyTimeGoal) entity.getGoal());
        failCount = entity.getFailCount();
        successCount = entity.getSuccessCount();
        relatedCertifications = new ArrayList<>();
    }
    public static ManyTimeCertificationResponse getSelectCertificationResponse(ManyTimeCertification entity, Set<Certification> relatedEntities,Set<ManyTimeGoalCertDate> certDates){
        return new ManyTimeCertificationResponse(entity, relatedEntities, certDates);
    }
    private ManyTimeCertificationResponse(ManyTimeCertification entity, Set<Certification> relatedEntities,Set<ManyTimeGoalCertDate> certDates){
        id = entity.getId();
        content = entity.getContent();
        picture = "/api/image/certification/"+entity.getId();
        state = entity.getState();
        date = entity.getDate();
        manyTimeGoal = ManyTimeGoalResponse.getInnerGoalResponse((ManyTimeGoal) entity.getGoal(),certDates);
        failCount = entity.getFailCount();
        successCount = entity.getSuccessCount();
        relatedCertifications = new ArrayList<>();
        relatedEntities.forEach(c -> relatedCertifications.add(new RelatedCertificationResponse(c.getId(),c.getDate(),c.getState())));
    }
    public static ManyTimeCertificationResponse getInnerCertificationResponse(ManyTimeCertification entity){
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
    public static ManyTimeCertificationResponse getInnerCertificationResponse(ManyTimeCertification entity, Set<Certification> relatedEntities){
        return new ManyTimeCertificationResponse(entity,relatedEntities);
    }
    private ManyTimeCertificationResponse(ManyTimeCertification entity,Set<Certification> relatedEntities){
        id = entity.getId();
        content = entity.getContent();
        picture = "/api/image/certification/"+entity.getId();
        state = entity.getState();
        date = entity.getDate();
        this.manyTimeGoal = null;
        failCount = entity.getFailCount();
        successCount = entity.getSuccessCount();
        relatedCertifications = new ArrayList<>();
        relatedEntities.forEach(c -> {
            if(c.getId()==entity.getId())return;
            if(c.getGoal().getId() != entity.getGoal().getId())return;
            relatedCertifications.add(new RelatedCertificationResponse(c.getId(), c.getDate(), c.getState()));
        });
    }
}

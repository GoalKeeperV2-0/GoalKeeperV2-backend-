package kr.co.goalkeeper.api.model.entity;

import kr.co.goalkeeper.api.exception.GoalkeeperException;
import kr.co.goalkeeper.api.model.request.ManyTimeCertificationRequest;
import kr.co.goalkeeper.api.model.response.ErrorMessage;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.io.File;
import java.time.LocalDate;

@Entity
@DiscriminatorValue("ManyTimeCertification")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ManyTimeCertification extends Certification {
    public ManyTimeCertification(ManyTimeCertificationRequest dto,Goal goal){
        content = dto.getContent();
        state = CertificationState.ONGOING;
        date = LocalDate.now();
        this.goal = goal;
        pictureFile = dto.getPicture();
    }
    @Override
    public void verificationSuccess() {
        int requiredSuccessCount = goal.requiredSuccessCount();
        successCount++;
        if(successCount>=requiredSuccessCount){
            success();
            ((ManyTimeGoal)goal).successCertification();
        }
    }

    @Override
    public void verificationFail() {
        int requiredSuccessCount = goal.requiredSuccessCount();
        failCount++;
        if(failCount>=Math.round(0.7 * requiredSuccessCount)){
            fail();
            ((ManyTimeGoal)goal).failCertification();
        }
    }
}

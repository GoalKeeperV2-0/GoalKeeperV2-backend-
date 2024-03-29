package kr.co.goalkeeper.api.model.entity.goal;

import kr.co.goalkeeper.api.model.request.OnetimeCertificationRequest;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.time.LocalDate;

@Entity
@DiscriminatorValue("OneTimeCertification")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OneTimeCertification extends Certification {
    public static OneTimeCertification getFailInstance(OneTimeGoal goal,LocalDate date){
        return new OneTimeCertification("작성한 인증내용이 없어요","",null,CertificationState.FAIL,date,0,goal,10000);
    }
    private OneTimeCertification(String content, String picture, MultipartFile pictureFile, CertificationState state, LocalDate date, int successCount, Goal goal, int failCount) {
        super(content, picture, pictureFile, state, date, successCount, goal, failCount);
    }

    public OneTimeCertification(OnetimeCertificationRequest dto, Goal goal){
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
        }
    }

    @Override
    public void verificationFail() {
        int requiredSuccessCount = goal.requiredSuccessCount();
        failCount++;
        if(failCount>=Math.round(0.7 * requiredSuccessCount)){ // 인증 상태가 실패로 바뀌어야 되는 경우
            fail();
        }
    }
    @Override
    protected void success() {
        super.success();
        goal.success();
    }

    @Override
    protected void fail() {
        super.fail();
        goal.failFromOngoing();
    }
    @Override
    public void changeGoalStateToWait(){
        goal.certCreated();
    }

    @Override
    public void timeOut() {
        fail();
    }
}

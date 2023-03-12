package kr.co.goalkeeper.api.model.entity;

import kr.co.goalkeeper.api.NotificationSender;
import kr.co.goalkeeper.api.model.request.ManyTimeCertificationRequest;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
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
            Notification notification = makeSuccessNotification();
            NotificationSender.send(notification);
        }
    }
    private Notification makeSuccessNotification(){
        return Notification.builder()
                .createdDate(LocalDate.now())
                .isRead(false)
                .receiver(goal.user)
                .notificationType(NotificationType.CERT_RESULT)
                .content("인증 성공").build();
    }

    @Override
    public void verificationFail() {
        int requiredSuccessCount = goal.requiredSuccessCount();
        failCount++;
        if(failCount>=Math.round(0.7 * requiredSuccessCount)){
            fail();
            ((ManyTimeGoal)goal).failCertification();
            Notification notification = makeFailNotification();
            NotificationSender.send(notification);
        }
    }

    @Override
    public void changeGoalStateToWait() {
        var certDates = ((ManyTimeGoal)goal).getCertDates();
        LocalDate lastCertDate = certDates.get(certDates.size()-1).getCertDate();
        if(this.date.equals(lastCertDate)){
            goal.certCreated();
        }
    }

    private Notification makeFailNotification(){
        return Notification.builder()
                .createdDate(LocalDate.now())
                .isRead(false)
                .receiver(goal.user)
                .notificationType(NotificationType.CERT_RESULT)
                .content("인증 실패").build();
    }
}

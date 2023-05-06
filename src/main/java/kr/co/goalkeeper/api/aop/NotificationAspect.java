package kr.co.goalkeeper.api.aop;

import kr.co.goalkeeper.api.model.entity.goal.Goal;
import kr.co.goalkeeper.api.model.entity.goal.Notification;
import kr.co.goalkeeper.api.model.entity.goal.NotificationType;
import kr.co.goalkeeper.api.model.entity.goal.User;
import kr.co.goalkeeper.api.model.request.GoalRequest;
import kr.co.goalkeeper.api.model.response.CertificationResponse;
import kr.co.goalkeeper.api.model.response.ManyTimeCertificationResponse;
import kr.co.goalkeeper.api.model.response.OneTimeCertificationResponse;
import kr.co.goalkeeper.api.model.response.Response;
import kr.co.goalkeeper.api.service.port.CredentialService;
import kr.co.goalkeeper.api.service.port.GoalGetService;
import kr.co.goalkeeper.api.service.port.NotificationService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Objects;

import static kr.co.goalkeeper.api.model.entity.goal.NotificationType.*;

@Aspect
@Component
@Slf4j
@Order(2)
@AllArgsConstructor
public class NotificationAspect {
    private final NotificationService notificationService;
    private final CredentialService credentialService;
    private final GoalGetService goalGetService;

    @AfterReturning(value = "execution(* kr.co.goalkeeper.api.controller.GoalController.addManyTimeGoal(..)) " +
            "|| execution(* kr.co.goalkeeper.api.controller.GoalController.addOneTimeGoal(..))")
    public void sendAddGoalNotification(JoinPoint joinPoint){
        log.info("목표 등록 알림 생성 시작");
        Object[] args = joinPoint.getArgs();
        String content = "'{title}' 목표등록이 완료되었어요! 지금 확인해보세요.";
        GoalRequest goalRequest = (GoalRequest)args[0];
        content = content.replace("{title}",goalRequest.getTitle());
        String accessToken = (String)args[1];
        Notification notification = makeNotification(GOAL_ADD,content,accessToken);
        notificationService.sendNotification(notification);
        log.info("목표 등록 알림 생성 완료");
    }
    private Notification makeNotification(NotificationType notificationType, String content, String accessToken){
        long userId = credentialService.getUserId(accessToken);
        User user = credentialService.getUserById(userId);
        return Notification.builder()
                .notificationType(notificationType)
                .createdDate(LocalDate.now())
                .isRead(false)
                .content(content)
                .receiver(user)
                .build();
    }
    @AfterReturning(value = "execution(* kr.co.goalkeeper.api.controller.CertificationController.createManyTimeCertificationByGoalId(..))",returning = "returnValue")
    public void sendAddManyTimeCertificationNotification(JoinPoint joinPoint,ResponseEntity<Response<ManyTimeCertificationResponse>> returnValue){
        log.info("인증 등록 알림 생성 시작");
        Object[] args = joinPoint.getArgs();
        long goalId = (long)args[0];
        String accessToken = (String)args[2];
        Response<ManyTimeCertificationResponse> returnResponse = returnValue.getBody();
        CertificationResponse certificationResponse = Objects.requireNonNull(returnResponse).getData();
        addCertificationNotification(certificationResponse,goalId,accessToken);
        log.info("인증 등록 알림 생성 완료");
    }
    private void addCertificationNotification(CertificationResponse certificationResponse,long goalId,String accessToken){
        LocalDate certDate = certificationResponse.getDate();
        Goal goal = goalGetService.getGoalById(goalId);
        String content = "'{title}'목표의 인증 등록이 '{date}'에 완료되었어요! 지금 확인해보세요.";
        content = content.replace("{title}",goal.getTitle()).replace("{date}",certDate.toString());
        Notification notification = makeNotification(CERT_ADD,content,accessToken);
        notificationService.sendNotification(notification);
    }
    @AfterReturning(value = "execution(* kr.co.goalkeeper.api.controller.CertificationController.createOneTimeCertificationByGoalId(..))",returning = "returnValue")
    public void sendAddOneTimeCertificationNotification(JoinPoint joinPoint,ResponseEntity<Response<OneTimeCertificationResponse>> returnValue){
        log.info("인증 등록 알림 생성 시작");
        Object[] args = joinPoint.getArgs();
        long goalId = (long)args[0];
        String accessToken = (String)args[2];
        Response<OneTimeCertificationResponse> returnResponse = returnValue.getBody();
        CertificationResponse certificationResponse = Objects.requireNonNull(returnResponse).getData();
        addCertificationNotification(certificationResponse,goalId,accessToken);
        log.info("인증 등록 알림 생성 완료");
    }

    @AfterReturning(value = "execution(* kr.co.goalkeeper.api.controller.GoalController.holdGoal(..))")
    public void sendHoldRequestNotification(JoinPoint joinPoint) {
        log.info("보류 요청 알림 생성 시작");
        Object[] args = joinPoint.getArgs();
        long goalId = (long) args[0];
        String accessToken = (String) args[1];
        String goalTitle = goalGetService.getGoalById(goalId).getTitle();
        String content = "'{title}' 목표의 검토신청이 완료되었어요! 검토가 완료되면 알림으로 알려드릴게요!";
        content = content.replace("{title}", goalTitle);
        Notification notification = makeNotification(HOLD_REQUEST, content, accessToken);
        notificationService.sendNotification(notification);
        log.info("보류 요청 알림 생성 완료");
    }
}

package kr.co.goalkeeper.api.util;

import kr.co.goalkeeper.api.model.entity.Notification;
import kr.co.goalkeeper.api.model.entity.NotificationType;
import kr.co.goalkeeper.api.model.entity.User;
import kr.co.goalkeeper.api.model.request.GoalRequest;
import kr.co.goalkeeper.api.model.response.Response;
import kr.co.goalkeeper.api.service.port.CredentialService;
import kr.co.goalkeeper.api.service.port.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Aspect
@Component
@Slf4j
@Order(2)
public class NotificationAspect {
    private final NotificationService notificationService;
    private final CredentialService credentialService;

    public NotificationAspect(NotificationService notificationService, CredentialService credentialService) {
        this.notificationService = notificationService;
        this.credentialService = credentialService;
    }

    @AfterReturning(value = "execution(* kr.co.goalkeeper.api.controller.GoalController.addManyTimeGoal(..)) " +
            "|| execution(* kr.co.goalkeeper.api.controller.GoalController.addOneTimeGoal(..))",returning = "returnValue")
    public void sendAddGoalNotification(JoinPoint joinPoint, ResponseEntity<Response<?>> returnValue){
        log.info("목표 등록 알림 생성");
        Object[] args = joinPoint.getArgs();
        String content = "'{title}' 목표등록이 완료되었어요! 지금 확인해보세요.";
        GoalRequest goalRequest = (GoalRequest)args[0];
        content = content.replace("{title}",goalRequest.getTitle());
        String accessToken = (String)args[1];
        long userId = credentialService.getUserId(accessToken);
        User user = credentialService.getUserById(userId);
        Notification notification = Notification.builder()
                .notificationType(NotificationType.GOAL_ADD)
                .createdDate(LocalDate.now())
                .isRead(false)
                .content(content)
                .receiver(user)
                .build();
        notificationService.sendNotification(notification);
    }
}

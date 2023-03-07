package kr.co.goalkeeper.api;

import kr.co.goalkeeper.api.model.entity.Notification;
import kr.co.goalkeeper.api.service.port.NotificationService;
import org.springframework.context.ApplicationContext;

public class NotificationSender {
    private static ApplicationContext applicationContext;
    static void init(ApplicationContext applicationContext){
        NotificationSender.applicationContext = applicationContext;
    }
    public static void send(Notification notification){
        NotificationService notificationService = applicationContext.getBean(NotificationService.class);
        notificationService.sendNotification(notification);
    }
}

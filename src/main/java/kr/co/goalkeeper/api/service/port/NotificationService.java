package kr.co.goalkeeper.api.service.port;

import kr.co.goalkeeper.api.model.entity.goal.Notification;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.util.List;

public interface NotificationService {
    void sendNotification(Notification notification);
    void sendNotification(List<Notification> notifications);
    Slice<Notification> getNotifications(long userId, Pageable pageable);
}

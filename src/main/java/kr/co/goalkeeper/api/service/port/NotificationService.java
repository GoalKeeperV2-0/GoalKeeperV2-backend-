package kr.co.goalkeeper.api.service.port;

import kr.co.goalkeeper.api.model.entity.Notification;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface NotificationService {
    void sendNotification(Notification notification);
    Slice<Notification> getNotifications(long userId, Pageable pageable);
}

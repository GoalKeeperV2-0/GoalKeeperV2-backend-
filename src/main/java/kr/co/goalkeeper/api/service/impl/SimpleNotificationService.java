package kr.co.goalkeeper.api.service.impl;

import kr.co.goalkeeper.api.model.entity.Notification;
import kr.co.goalkeeper.api.repository.NotificationRepository;
import kr.co.goalkeeper.api.service.port.NotificationService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

@Service
public class SimpleNotificationService implements NotificationService {
    private final NotificationRepository notificationRepository;

    public SimpleNotificationService(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    @Override
    public void sendNotification(Notification notification) {
        notificationRepository.save(notification);
    }

    @Override
    public Slice<Notification> getNotifications(long userId, Pageable pageable) {
        return notificationRepository.findAllByReceiver_Id(userId,pageable);
    }
}

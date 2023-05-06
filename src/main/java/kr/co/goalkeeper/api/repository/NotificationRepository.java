package kr.co.goalkeeper.api.repository;

import kr.co.goalkeeper.api.model.entity.goal.Notification;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationRepository extends JpaRepository<Notification,Long> {
    Slice<Notification> findAllByReceiver_Id(long userId, Pageable pageable);
}

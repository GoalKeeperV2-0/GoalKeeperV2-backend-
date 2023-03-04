package kr.co.goalkeeper.api.model.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @ManyToOne
    @JoinColumn(name = "receiver_id")
    private User receiver;
    @Enumerated(EnumType.STRING)
    private NotificationType notificationType;
    private String content;
    private boolean isRead;
    private LocalDate createdDate;
    @Builder
    private Notification(long id, User receiver, NotificationType notificationType, String content, boolean isRead, LocalDate createdDate) {
        this.id = id;
        this.receiver = receiver;
        this.notificationType = notificationType;
        this.content = content;
        this.isRead = isRead;
        this.createdDate = createdDate;
    }
}

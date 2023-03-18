package kr.co.goalkeeper.api.model.entity;

import kr.co.goalkeeper.api.NotificationSender;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static kr.co.goalkeeper.api.model.entity.GoalState.*;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public abstract class Goal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected long id;
    @ManyToOne(cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    protected User user;
    @NotNull
    protected String title;
    @NotNull
    protected String content;
    @Min(0)
    @Max(10000)
    protected int point;
    @Column
    @Enumerated(EnumType.STRING)
    @NotNull
    protected GoalState goalState;
    @Column
    @Enumerated(EnumType.STRING)
    @NotNull
    protected Reward reward;
    @OneToOne
    @JoinColumn(name = "category")
    protected Category category;
    @Column
    @NotNull
    protected LocalDate startDate;
    @Column
    @NotNull
    protected LocalDate endDate;
    @OneToMany(mappedBy = "goal",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    protected List<Certification> certificationList = new ArrayList<>();
    private boolean holdRequestAble = true;

    protected Goal(long id, User user, String title, String content, int point, GoalState goalState, Reward reward, Category category) {
        this.id = id;
        this.user = user;
        this.title = title;
        this.content = content;
        this.point = point;
        this.goalState = goalState;
        this.reward = reward;
        this.category = category;
    }
    protected final void minusUserPoint(){
        if(user!=null){
            user.minusPoint(point);
        }
    }
    public final int requiredSuccessCount(){
        if(point<=500){
            return 5;
        }else if(point<=1000){
            return 10;
        } else if (point<=5000) {
            return 20;
        }else{
            return 30;
        }
    }
    public final void success(){
        goalState = SUCCESS;
        user.plusPoint(Math.round(point*reward.getRewardRate()),category.getCategoryType());
        Notification notification = makesuccessNotification();
        NotificationSender.send(notification);
    }
    private Notification makesuccessNotification(){
        return Notification.builder()
                .createdDate(LocalDate.now())
                .isRead(false)
                .receiver(user)
                .notificationType(NotificationType.GOAL_RESULT)
                .content("목표 성공").build();
    }
    public final void hold(){
        goalState = HOLD;
        holdRequestAble = false;
    }

    public void certCreated(){
        goalState = WAITING_CERT_COMPLETE;
    }

    public final void failFromOngoing(){
        goalState = FAIL;
        holdRequestAble = true;
        certificationList.forEach(c->{
            if(c.state==CertificationState.ONGOING){
                c.state = CertificationState.FAIL;
            }
        });
        minusUserCategoryPoint();
        Notification notification = makeFailNotification();
        NotificationSender.send(notification);
    }
    private Notification makeFailNotification(){
        return Notification.builder()
                .createdDate(LocalDate.now())
                .isRead(false)
                .receiver(user)
                .notificationType(NotificationType.GOAL_RESULT)
                .content("목표 실패").build();
    }
    private void minusUserCategoryPoint(){
        if(user!=null){
            user.minusCategoryPoint(point,category.getCategoryType());
        }
    }
    public Notification makeDDayNotification(String content){
        return Notification.builder()
                .createdDate(endDate)
                .isRead(false)
                .receiver(user)
                .notificationType(NotificationType.D_DAY)
                .content(content)
                .build();
    }
}

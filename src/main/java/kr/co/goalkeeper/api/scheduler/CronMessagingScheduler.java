package kr.co.goalkeeper.api.scheduler;

import kr.co.goalkeeper.api.NotificationSender;
import kr.co.goalkeeper.api.model.entity.goal.Goal;
import kr.co.goalkeeper.api.model.entity.goal.GoalState;
import kr.co.goalkeeper.api.model.entity.goal.ManyTimeGoal;
import kr.co.goalkeeper.api.model.entity.goal.Notification;
import kr.co.goalkeeper.api.repository.GoalBatchRepository;
import kr.co.goalkeeper.api.repository.ManyTimeGoalBatchRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@EnableScheduling
@EnableAsync
@Component
@Slf4j
@AllArgsConstructor
public class CronMessagingScheduler implements OnlyMessagingScheduler {
    private final GoalBatchRepository goalBatchRepository;
    private final ManyTimeGoalBatchRepository manyTimeGoalBatchRepository;
    @Transactional
    @Scheduled(cron="0 0 0 * * *")
    @Async
    @Override
    public void sendCertDDayMessage() {
        LocalDate today = LocalDate.now();
        checkCertDDay(today,0);
    }
    private void checkCertDDay(LocalDate endDate,int page) {
        Slice<ManyTimeGoal> goals =
                manyTimeGoalBatchRepository.findAllByCertDatesContaining(endDate.toString(),PageRequest.of(page,100));
        List<Notification> notifications = makeNotifications(goals,"인증날이 다가왔어요!");
        NotificationSender.send(notifications);
        if(goals.hasNext()){
            checkGoalDDay(endDate,page+1);
        }
    }

    @Transactional
    @Scheduled(cron="0 3 0 * * *")
    @Async
    @Override
    public void sendGoalDDayMessage() {
        LocalDate endDate = LocalDate.now();
        checkGoalDDay(endDate,0);
    }
    private void checkGoalDDay(LocalDate endDate,int page){
        Slice<Goal> goals = goalBatchRepository.findAllByEndDateAndGoalState(endDate,GoalState.ONGOING, PageRequest.of(page,100));
        List<Notification> notifications = makeNotifications(goals,"목표 마감일이 다가왔어요!");
        NotificationSender.send(notifications);
        if(goals.hasNext()){
            checkGoalDDay(endDate,page+1);
        }
    }
    private <T extends Goal> List<Notification> makeNotifications(Slice<T> goals,String message){
        List<Notification> notifications = new ArrayList<>();
        for (T goal : goals) {
            Notification notification = goal.makeDDayNotification(message);
            if(notification == null) continue;
            notifications.add(notification);
        }
        return notifications;
    }
}

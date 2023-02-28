package kr.co.goalkeeper.api.scheduler;

import kr.co.goalkeeper.api.repository.GoalBatchRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
@EnableScheduling
@EnableAsync
@Component
@Slf4j
public class CronChangeGoalScheduler implements ChangeGoalScheduler {
    private final GoalBatchRepository goalBatchRepository;

    public CronChangeGoalScheduler(GoalBatchRepository goalBatchRepository ) {
        this.goalBatchRepository = goalBatchRepository;
    }

    @Override
    @Scheduled(cron="0 0 * * * ?")
    @Async
    public void checkTimeOutGoal() {
        goalBatchRepository.updateTimeOutGoal();
        log.info("------checkTimeOutGoal------");
    }

    @Override
    @Scheduled(cron="0 0 * * * ?")
    @Async
    public void checkTimeOutGoalButCertRemain() {
        goalBatchRepository.updateTimeOutGoalCertRemain();
        log.info("------checkTimeOutGoalButCertRemain------");
    }

    @Transactional
    @Override
    @Scheduled(cron="0 0 * * * ?")
    @Async
    public void checkNoCertificationAtCertDay() {
        goalBatchRepository.addFailCountAtNoCertAtCertDay();
        goalBatchRepository.updateManyTimeGoalToFail();
        log.info("------checkNoCertificationAtCertDay------");
    }
}

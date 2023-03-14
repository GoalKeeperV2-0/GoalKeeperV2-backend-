package kr.co.goalkeeper.api.scheduler;

import kr.co.goalkeeper.api.model.entity.*;
import kr.co.goalkeeper.api.repository.ManyTimeGoalBatchRepository;
import kr.co.goalkeeper.api.repository.OneTimeGoalBatchRepository;
import kr.co.goalkeeper.api.service.port.ManyTimeCertificationService;
import kr.co.goalkeeper.api.service.port.OneTimeCertificationService;
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
import java.util.List;

@EnableScheduling
@EnableAsync
@Component
@Slf4j
@AllArgsConstructor
public class CronChangeGoalSchedulerV2 implements ChangeGoalScheduler {
    private OneTimeGoalBatchRepository goalBatchRepository;
    private OneTimeCertificationService oneTimeCertificationService;
    private ManyTimeCertificationService manyTimeCertificationService;
    private ManyTimeGoalBatchRepository manyTimeGoalBatchRepository;

    @Transactional
    @Scheduled(cron="0 0 0 * * *")
    @Async
    @Override
    public void checkTimeOutGoal() {
        LocalDate endDate = LocalDate.now().minusDays(1);
        addFailCertToOneTimeGoals(endDate,0);
        addFailCertToManyTimeGoals(endDate,0);
    }
    private void addFailCertToOneTimeGoals(LocalDate endDate,int page){
        Slice<OneTimeGoal> oneTimeGoals = goalBatchRepository.findAllByGoalStateAndEndDate(GoalState.ONGOING,endDate,PageRequest.of(page,100));
        List<OneTimeGoal> oneTimeGoalList = oneTimeGoals.getContent();
        for (OneTimeGoal oneTimeGoal:oneTimeGoalList) {
            OneTimeCertification oneTimeCertification = OneTimeCertification.getFailInstance(oneTimeGoal,endDate);
            oneTimeCertificationService.createCertification(oneTimeCertification,oneTimeGoal.getUser().getId());
            oneTimeCertification.verificationFail();
        }
        if(oneTimeGoals.hasNext()){
            addFailCertToOneTimeGoals(endDate,page+1);
        }
    }
    private void addFailCertToManyTimeGoals(LocalDate endDate,int page){
        Slice<ManyTimeGoal> manyTimeGoals = manyTimeGoalBatchRepository.findAllByGoalStateAndEndDate(GoalState.ONGOING,endDate,PageRequest.of(page,100));
        List<ManyTimeGoal> manyTimeGoalList = manyTimeGoals.getContent();
        for (ManyTimeGoal manyTimeGoal:manyTimeGoalList) {
            ManyTimeCertification manyTimeCertification = ManyTimeCertification.getFailInstance(manyTimeGoal,endDate);
            manyTimeCertificationService.createCertification(manyTimeCertification,manyTimeGoal.getUser().getId());
            manyTimeCertification.verificationFail();
        }
        if(manyTimeGoals.hasNext()){
            addFailCertToOneTimeGoals(endDate,page+1);
        }
    }

    @Override
    public void checkTimeOutGoalButCertRemain() {

    }

    @Override
    public void checkNoCertificationAtCertDay() {

    }
}

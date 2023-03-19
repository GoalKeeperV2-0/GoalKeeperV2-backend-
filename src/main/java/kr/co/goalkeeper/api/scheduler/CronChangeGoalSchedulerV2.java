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
    @Scheduled(cron="0 6 0 * * *")
    @Async
    @Override
    public void checkTimeOutGoal() {
        LocalDate endDate = LocalDate.now().minusDays(1);
        checkOneTimeGoal(endDate,0);
        checkManyTimeGoal(endDate,0);
    }
    private void checkOneTimeGoal(LocalDate endDate, int page){
        Slice<OneTimeGoal> oneTimeGoals = goalBatchRepository.findAllByGoalStateAndEndDate(GoalState.ONGOING,endDate,PageRequest.of(page,100));
        addFailCertToOneTimeGoal(oneTimeGoals,endDate);
        if(oneTimeGoals.hasNext()){
            checkOneTimeGoal(endDate,page+1);
        }
    }
    private void addFailCertToOneTimeGoal(Slice<OneTimeGoal> oneTimeGoals,LocalDate endDate){
        for (OneTimeGoal oneTimeGoal:oneTimeGoals) {
            OneTimeCertification oneTimeCertification = OneTimeCertification.getFailInstance(oneTimeGoal,endDate);
            oneTimeCertificationService.createCertification(oneTimeCertification,oneTimeGoal.getUser().getId());
            oneTimeCertification.verificationFail();
        }
    }
    private void checkManyTimeGoal(LocalDate endDate, int page){
        Slice<ManyTimeGoal> manyTimeGoals = manyTimeGoalBatchRepository.findAllByGoalStateAndEndDate(GoalState.ONGOING,endDate,PageRequest.of(page,100));
        addFailCertToManyTimeGoal(manyTimeGoals,endDate);
        if(manyTimeGoals.hasNext()){
            checkManyTimeGoal(endDate,page+1);
        }
    }
    private void addFailCertToManyTimeGoal(Slice<ManyTimeGoal> manyTimeGoals, LocalDate endDate){
        for (ManyTimeGoal manyTimeGoal:manyTimeGoals) {
            ManyTimeCertification manyTimeCertification = ManyTimeCertification.getFailInstance(manyTimeGoal,endDate);
            manyTimeCertificationService.createCertification(manyTimeCertification,manyTimeGoal.getUser().getId());
            manyTimeCertification.verificationFail();
        }
    }

    @Transactional
    @Scheduled(cron="0 9 0 * * *")
    @Async
    @Override
    public void checkNoCertificationAtCertDay() {
        LocalDate certDate = LocalDate.now().minusDays(1);
        checkNoCert(certDate,0);
    }
    private void checkNoCert(LocalDate certDate, int page){
        Slice<ManyTimeGoal> noCertAtCertDate = manyTimeGoalBatchRepository.findAllByCertDatesContaining(certDate.toString(),PageRequest.of(0,100));
        addFailCertToManyTimeGoal(noCertAtCertDate,certDate);
        if(noCertAtCertDate.hasNext()){
            checkNoCert(certDate,page+1);
        }
    }
}

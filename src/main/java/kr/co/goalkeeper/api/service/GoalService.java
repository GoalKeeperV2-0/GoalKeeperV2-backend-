package kr.co.goalkeeper.api.service;

import kr.co.goalkeeper.api.exception.GoalkeeperException;
import kr.co.goalkeeper.api.model.entity.*;
import kr.co.goalkeeper.api.model.response.ErrorMessage;
import kr.co.goalkeeper.api.repository.ManyTimeGoalRepository;
import kr.co.goalkeeper.api.repository.OneTimeGoalRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;

@Service
public class GoalService implements OneTimeGoalService,ManyTimeGoalService{
    private final ManyTimeGoalRepository manyTimeGoalRepository;
    private final OneTimeGoalRepository oneTimeGoalRepository;
    private static final int PAGE_SIZE = 9;


    public GoalService(ManyTimeGoalRepository manyTimeGoalRepository, OneTimeGoalRepository oneTimeGoalRepository) {
        this.manyTimeGoalRepository = manyTimeGoalRepository;
        this.oneTimeGoalRepository = oneTimeGoalRepository;
    }

    @Override
    public ManyTimeGoal createManyTimeGoal(ManyTimeGoal manyTimeGoal) {
        if(!validateStartDateAndEndDate(manyTimeGoal)) {
            ErrorMessage errorMessage = new ErrorMessage(400,"목표 시작날짜는 목표 종료날짜보다 4일 이상 빨라야 합니다." +
                    "\n목표 시작날짜와 목표 종료날짜는 오늘보다 빠르면 안됩니다.");
            throw new GoalkeeperException(errorMessage);
        }
        if(!validatePoint(manyTimeGoal)){
            ErrorMessage errorMessage = new ErrorMessage(409,"포인트가 부족 합니다.");
            throw new GoalkeeperException(errorMessage);
        }
        if(!validateCertDates(manyTimeGoal)){
            ErrorMessage errorMessage = new ErrorMessage(409,"인증 날은 마지막 날을 포함해 최소 4일 이상이어야 합니다.");
            throw new GoalkeeperException(errorMessage);
        }
        return manyTimeGoalRepository.save(manyTimeGoal);
    }
    private boolean validateStartDateAndEndDate(ManyTimeGoal manyTimeGoal){
        LocalDate now = LocalDate.now();
        LocalDate start = manyTimeGoal.getStartDate();
        LocalDate end = manyTimeGoal.getEndDate();
        if(start.isBefore(now)|| end.isBefore(now) || end.isBefore(start)) return false;
        Period period = Period.between(start,end);
        return period.getDays()>=4;
    }
    private boolean validatePoint(ManyTimeGoal manyTimeGoal){
        User user = manyTimeGoal.getUser();
        return user.getPoint()>manyTimeGoal.getPoint();
    }
    private boolean validateCertDates(ManyTimeGoal manyTimeGoal){
        List<ManyTimeGoalCertDate> certDates = manyTimeGoal.getCertDates();
        boolean validateCertDatesCount = certDates.size()>=4;
        boolean validateCertDatesContainEndDates = certDates.stream().anyMatch(certDate-> certDate.getCertDate().equals(manyTimeGoal.getEndDate()));
        return validateCertDatesCount && validateCertDatesContainEndDates;
    }

    @Override
    public ManyTimeGoal getManyTimeGoalById(long goalId) {
        return manyTimeGoalRepository.findById(goalId).orElseThrow(() -> {
            ErrorMessage errorMessage = new ErrorMessage(404,"없는 goalId 입니다.");
            return new GoalkeeperException(errorMessage);
        });
    }

    @Override
    public Page<ManyTimeGoal> getManyTimeGoalsByUserId(long userId,int page) {
        return manyTimeGoalRepository.findAllByUser_Id(userId,PageRequest.of(page,PAGE_SIZE));
    }

    @Override
    public Page<ManyTimeGoal> getManyTimeGoalsByUserIdAndCategory(long userId, CategoryType categoryType,int page) {
        return manyTimeGoalRepository.findAllByUser_IdAndCategory_CategoryType(userId,categoryType, PageRequest.of(page,PAGE_SIZE));
    }

    @Override
    public OneTimeGoal createOneTimeGoal(OneTimeGoal oneTimeGoal) {
        return oneTimeGoalRepository.save(oneTimeGoal);
    }

    @Override
    public OneTimeGoal getOneTimeGoalById(long goalId) {
        return oneTimeGoalRepository.findById(goalId).orElseThrow(() -> {
            ErrorMessage errorMessage = new ErrorMessage(404,"없는 goalId 입니다.");
            return new GoalkeeperException(errorMessage);
        });
    }

    @Override
    public Page<OneTimeGoal> getOneTimeGoalsByUserId(long userId,int page) {
        return oneTimeGoalRepository.findAllByUser_Id(userId,PageRequest.of(page,PAGE_SIZE));
    }

    @Override
    public Page<OneTimeGoal> getOneTimeGoalsByUserIdAndCategory(long userId, CategoryType categoryType,int page) {
        return oneTimeGoalRepository.findAllByUser_IdAndCategory_CategoryType(userId,categoryType, PageRequest.of(page,PAGE_SIZE));
    }
}

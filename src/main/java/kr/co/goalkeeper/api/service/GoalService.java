package kr.co.goalkeeper.api.service;

import kr.co.goalkeeper.api.exception.GoalkeeperException;
import kr.co.goalkeeper.api.model.entity.CategoryType;
import kr.co.goalkeeper.api.model.entity.ManyTimeGoal;
import kr.co.goalkeeper.api.model.entity.OneTimeGoal;
import kr.co.goalkeeper.api.model.response.ErrorMessage;
import kr.co.goalkeeper.api.repository.ManyTimeGoalRepository;
import kr.co.goalkeeper.api.repository.OneTimeGoalRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
@Service
public class GoalService implements OneTimeGoalService,ManyTimeGoalService{
    private final ManyTimeGoalRepository manyTimeGoalRepository;
    private final OneTimeGoalRepository oneTimeGoalRepository;

    public GoalService(ManyTimeGoalRepository manyTimeGoalRepository, OneTimeGoalRepository oneTimeGoalRepository) {
        this.manyTimeGoalRepository = manyTimeGoalRepository;
        this.oneTimeGoalRepository = oneTimeGoalRepository;
    }

    @Override
    public long createManyTimeGoal(ManyTimeGoal manyTimeGoal) {
        LocalDate start = manyTimeGoal.getStartDate();
        LocalDate end = manyTimeGoal.getEndDate();
        Period period = Period.between(start,end);
        if(period.getDays()>=4)
            return manyTimeGoalRepository.save(manyTimeGoal).getId();
        else {
            ErrorMessage errorMessage = new ErrorMessage(400,"목표 시작날짜는 목표 종료날짜보다 4일 이상 빨라야 합니다.");
            throw new GoalkeeperException(errorMessage);
        }
    }

    @Override
    public ManyTimeGoal getManyTimeGoalById(long goalId) {
        return null;
    }

    @Override
    public List<ManyTimeGoal> getManyTimeGoalsByUserId(long userId) {
        return null;
    }

    @Override
    public List<ManyTimeGoal> getManyTimeGoalsByUserIdAndCategory(long userId, CategoryType categoryType) {
        return null;
    }

    @Override
    public long createOneTimeGoal(OneTimeGoal oneTimeGoal) {
        return oneTimeGoalRepository.save(oneTimeGoal).getId();
    }

    @Override
    public OneTimeGoal getOneTimeGoalById(long goalId) {
        return null;
    }

    @Override
    public List<OneTimeGoal> getOneTimeGoalsByUserId(long userId) {
        return null;
    }

    @Override
    public List<OneTimeGoal> getOneTimeGoalsByUserIdAndCategory(long userId, CategoryType categoryType) {
        return null;
    }
}

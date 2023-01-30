package kr.co.goalkeeper.api.service;

import kr.co.goalkeeper.api.exception.GoalkeeperException;
import kr.co.goalkeeper.api.model.entity.Category;
import kr.co.goalkeeper.api.model.entity.CategoryType;
import kr.co.goalkeeper.api.model.entity.ManyTimeGoal;
import kr.co.goalkeeper.api.model.entity.OneTimeGoal;
import kr.co.goalkeeper.api.model.response.ErrorMessage;
import kr.co.goalkeeper.api.repository.ManyTimeGoalRepository;
import kr.co.goalkeeper.api.repository.OneTimeGoalRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.function.Supplier;

@Service
public class GoalService implements OneTimeGoalService,ManyTimeGoalService{
    private final ManyTimeGoalRepository manyTimeGoalRepository;
    private final OneTimeGoalRepository oneTimeGoalRepository;
    private static int PAGE_SIZE = 9;


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

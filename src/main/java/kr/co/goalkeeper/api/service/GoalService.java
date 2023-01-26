package kr.co.goalkeeper.api.service;

import kr.co.goalkeeper.api.model.entity.CategoryType;
import kr.co.goalkeeper.api.model.entity.ManyTimeGoal;
import kr.co.goalkeeper.api.model.entity.OneTimeGoal;
import kr.co.goalkeeper.api.repository.ManyTimeGoalRepository;
import kr.co.goalkeeper.api.repository.OneTimeGoalRepository;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class GoalService implements OnTimeGoalService,ManyTimeGoalService{
    private final ManyTimeGoalRepository manyTimeGoalRepository;
    private final OneTimeGoalRepository oneTimeGoalRepository;

    public GoalService(ManyTimeGoalRepository manyTimeGoalRepository, OneTimeGoalRepository oneTimeGoalRepository) {
        this.manyTimeGoalRepository = manyTimeGoalRepository;
        this.oneTimeGoalRepository = oneTimeGoalRepository;
    }

    @Override
    public void createManyTimeGoal(ManyTimeGoal manyTimeGoal) {

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
    public void createOneTimeGoal(OneTimeGoal oneTimeGoal) {

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

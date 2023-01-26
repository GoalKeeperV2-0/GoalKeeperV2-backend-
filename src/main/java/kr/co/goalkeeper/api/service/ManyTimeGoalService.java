package kr.co.goalkeeper.api.service;

import kr.co.goalkeeper.api.model.domain.CategoryType;
import kr.co.goalkeeper.api.model.domain.ManyTimeGoal;

import java.util.List;

public interface ManyTimeGoalService {
    void createManyTimeGoal(ManyTimeGoal manyTimeGoal);
    ManyTimeGoal getManyTimeGoalById(long goalId);
    List<ManyTimeGoal> getManyTimeGoalsByUserId(long userId);
    List<ManyTimeGoal> getManyTimeGoalsByUserIdAndCategory(long userId, CategoryType categoryType);
}

package kr.co.goalkeeper.api.service;

import kr.co.goalkeeper.api.model.entity.CategoryType;
import kr.co.goalkeeper.api.model.entity.ManyTimeGoal;

import java.util.List;

public interface ManyTimeGoalService {
    long createManyTimeGoal(ManyTimeGoal manyTimeGoal);
    ManyTimeGoal getManyTimeGoalById(long goalId);
    List<ManyTimeGoal> getManyTimeGoalsByUserId(long userId);
    List<ManyTimeGoal> getManyTimeGoalsByUserIdAndCategory(long userId, CategoryType categoryType);
}

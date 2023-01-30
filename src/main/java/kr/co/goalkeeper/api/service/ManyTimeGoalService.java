package kr.co.goalkeeper.api.service;

import kr.co.goalkeeper.api.model.entity.CategoryType;
import kr.co.goalkeeper.api.model.entity.ManyTimeGoal;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ManyTimeGoalService {
    long createManyTimeGoal(ManyTimeGoal manyTimeGoal);
    ManyTimeGoal getManyTimeGoalById(long goalId);
    Page<ManyTimeGoal> getManyTimeGoalsByUserId(long userId,int page);
    Page<ManyTimeGoal> getManyTimeGoalsByUserIdAndCategory(long userId, CategoryType categoryType,int page);
}

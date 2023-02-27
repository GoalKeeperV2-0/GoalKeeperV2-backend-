package kr.co.goalkeeper.api.service.port;

import kr.co.goalkeeper.api.model.entity.CategoryType;
import kr.co.goalkeeper.api.model.entity.Goal;
import org.springframework.data.domain.Page;

public interface GoalGetService {
    Goal getGoalById(long goalId);
    Goal getMyGoalById(long userId,long goalId);
    Page<Goal> getGoalsByUserId(long userId, int page);
    Page<Goal> getGoalsByUserIdAndCategory(long userId, CategoryType categoryType, int page);
}

package kr.co.goalkeeper.api.service.port;

import kr.co.goalkeeper.api.model.entity.CategoryType;
import kr.co.goalkeeper.api.model.entity.goal.Goal;
import kr.co.goalkeeper.api.model.entity.goal.GoalState;
import kr.co.goalkeeper.api.model.response.GoalResponse;
import org.springframework.data.domain.Page;

import java.util.List;

public interface GoalGetService {
    Goal getGoalById(long goalId);
    Goal getMyGoalById(long userId,long goalId);
    Page<GoalResponse> getGoalsByUserId(long userId, int page);
    Page<GoalResponse> getGoalsByUserIdAndState(long userId, GoalState goalState, int page);
    Page<GoalResponse> getGoalsByUserIdAndCategory(long userId, CategoryType categoryType, int page);
    Page<GoalResponse> getGoalsByUserIdAndCategoryAndState(long userId, CategoryType categoryType,GoalState goalState, int page);
    List<GoalResponse> getGoalsByUSerIdAndNeedCertNow(long userId);
}

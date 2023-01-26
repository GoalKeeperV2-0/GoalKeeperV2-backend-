package kr.co.goalkeeper.api.service;

import kr.co.goalkeeper.api.model.domain.CategoryType;
import kr.co.goalkeeper.api.model.domain.OneTimeGoal;

import java.util.List;

public interface OnTimeGoalService {
    void createOneTimeGoal(OneTimeGoal oneTimeGoal);
    OneTimeGoal getOneTimeGoalById(long goalId);
    List<OneTimeGoal> getOneTimeGoalsByUserId(long userId);
    List<OneTimeGoal> getOneTimeGoalsByUserIdAndCategory(long userId, CategoryType categoryType);
}

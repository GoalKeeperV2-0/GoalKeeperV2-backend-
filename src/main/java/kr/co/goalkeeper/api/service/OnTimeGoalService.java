package kr.co.goalkeeper.api.service;

import kr.co.goalkeeper.api.model.entity.CategoryType;
import kr.co.goalkeeper.api.model.entity.OneTimeGoal;

import java.util.List;

public interface OnTimeGoalService {
    void createOneTimeGoal(OneTimeGoal oneTimeGoal);
    OneTimeGoal getOneTimeGoalById(long goalId);
    List<OneTimeGoal> getOneTimeGoalsByUserId(long userId);
    List<OneTimeGoal> getOneTimeGoalsByUserIdAndCategory(long userId, CategoryType categoryType);
}

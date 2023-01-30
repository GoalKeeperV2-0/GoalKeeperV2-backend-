package kr.co.goalkeeper.api.service;

import kr.co.goalkeeper.api.model.entity.CategoryType;
import kr.co.goalkeeper.api.model.entity.OneTimeGoal;
import org.springframework.data.domain.Page;

import java.util.List;

public interface OneTimeGoalService {
    OneTimeGoal createOneTimeGoal(OneTimeGoal oneTimeGoal);
    OneTimeGoal getOneTimeGoalById(long goalId);
    Page<OneTimeGoal> getOneTimeGoalsByUserId(long userId,int page);
    Page<OneTimeGoal> getOneTimeGoalsByUserIdAndCategory(long userId, CategoryType categoryType, int page);
}

package kr.co.goalkeeper.api.service.port;

import kr.co.goalkeeper.api.model.entity.CategoryType;
import kr.co.goalkeeper.api.model.entity.OneTimeGoal;
import org.springframework.data.domain.Page;

public interface OneTimeGoalService {
    OneTimeGoal createOneTimeGoal(OneTimeGoal oneTimeGoal);
}

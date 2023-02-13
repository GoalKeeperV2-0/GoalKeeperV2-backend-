package kr.co.goalkeeper.api.service.port;

import kr.co.goalkeeper.api.model.entity.CategoryType;
import kr.co.goalkeeper.api.model.entity.ManyTimeGoal;
import org.springframework.data.domain.Page;

public interface ManyTimeGoalService {
    ManyTimeGoal createManyTimeGoal(ManyTimeGoal manyTimeGoal);
}

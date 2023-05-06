package kr.co.goalkeeper.api.service.port;

import kr.co.goalkeeper.api.model.entity.goal.User;

public interface HoldGoalService {
    void holdGoal(User user, long goalId);
}

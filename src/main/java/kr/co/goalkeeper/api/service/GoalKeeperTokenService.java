package kr.co.goalkeeper.api.service;

import kr.co.goalkeeper.api.model.domain.GoalKeeperToken;
import kr.co.goalkeeper.api.model.domain.User;

public interface GoalKeeperTokenService {
    GoalKeeperToken createToken(User user);
}

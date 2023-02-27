package kr.co.goalkeeper.api.service.impl;

import kr.co.goalkeeper.api.exception.GoalkeeperException;
import kr.co.goalkeeper.api.model.entity.User;
import kr.co.goalkeeper.api.model.response.ErrorMessage;
import kr.co.goalkeeper.api.model.response.PointState;
import kr.co.goalkeeper.api.model.response.Statistics;
import kr.co.goalkeeper.api.repository.GoalRepository;
import kr.co.goalkeeper.api.repository.UserRepository;
import kr.co.goalkeeper.api.service.port.StatisticsService;
import org.springframework.stereotype.Service;

import static kr.co.goalkeeper.api.model.entity.GoalState.*;
import static kr.co.goalkeeper.api.model.entity.GoalState.FAIL;

@Service
public class SimpleStatisticsService implements StatisticsService {
    private final GoalRepository goalRepository;
    private final UserRepository userRepository;

    public SimpleStatisticsService(GoalRepository goalRepository, UserRepository userRepository) {
        this.goalRepository = goalRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Statistics getTotalGoalStatistics() {
        long totalCount = goalRepository.count();
        long successCount = goalRepository.countAllByGoalState(SUCCESS);
        long failCount = goalRepository.countAllByGoalState(FAIL);
        long onGoingCount = goalRepository.countAllByGoalState(ONGOING);
        return new Statistics(totalCount,successCount,failCount,onGoingCount);
    }

    @Override
    public Statistics getGoalStatisticsByUserId(long userId) {
        long totalCount = goalRepository.countAllByUser_Id(userId);
        long successCount = goalRepository.countAllByGoalStateAndUser_Id(SUCCESS,userId);
        long failCount = goalRepository.countAllByGoalStateAndUser_Id(FAIL,userId);
        long onGoingCount = goalRepository.countAllByGoalStateAndUser_Id(ONGOING,userId);
        return new Statistics(totalCount,successCount,failCount,onGoingCount);
    }

    @Override
    public PointState getPointStateByUserId(long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> {
            ErrorMessage errorMessage = new ErrorMessage(404,"없는 유저입니다.");
            return new GoalkeeperException(errorMessage);
        });
        return new PointState(user);
    }
}

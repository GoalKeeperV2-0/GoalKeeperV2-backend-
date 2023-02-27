package kr.co.goalkeeper.api.service.impl;

import kr.co.goalkeeper.api.model.response.Statistics;
import kr.co.goalkeeper.api.repository.GoalRepository;
import kr.co.goalkeeper.api.service.port.StatisticsService;
import org.springframework.stereotype.Service;

import static kr.co.goalkeeper.api.model.entity.GoalState.*;
import static kr.co.goalkeeper.api.model.entity.GoalState.FAIL;

@Service
public class SimpleStatisticsService implements StatisticsService {
    private final GoalRepository goalRepository;

    public SimpleStatisticsService(GoalRepository goalRepository) {
        this.goalRepository = goalRepository;
    }

    @Override
    public Statistics getTotalStatistics() {
        long totalCount = goalRepository.count();
        long successCount = goalRepository.countAllByGoalState(SUCCESS);
        long failCount = goalRepository.countAllByGoalState(FAIL);
        long onGoingCount = goalRepository.countAllByGoalState(ONGOING);
        return new Statistics(totalCount,successCount,failCount,onGoingCount);
    }
}

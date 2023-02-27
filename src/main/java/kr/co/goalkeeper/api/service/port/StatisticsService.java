package kr.co.goalkeeper.api.service.port;

import kr.co.goalkeeper.api.model.response.PointState;
import kr.co.goalkeeper.api.model.response.Statistics;

public interface StatisticsService {
    Statistics getTotalGoalStatistics();
    Statistics getGoalStatisticsByUserId(long userId);
    PointState getPointStateByUserId(long userId);

}

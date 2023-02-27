package kr.co.goalkeeper.api.controller;

import kr.co.goalkeeper.api.model.response.PointState;
import kr.co.goalkeeper.api.model.response.Response;
import kr.co.goalkeeper.api.model.response.Statistics;
import kr.co.goalkeeper.api.service.port.CredentialService;
import kr.co.goalkeeper.api.service.port.StatisticsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/statistics")
public class StatisticsController {
    private final StatisticsService statisticsService;
    private final CredentialService credentialService;

    public StatisticsController(StatisticsService statisticsService, CredentialService credentialService) {
        this.statisticsService = statisticsService;
        this.credentialService = credentialService;
    }

    @GetMapping("")
    public ResponseEntity<Response<Statistics>> getTotalGoalStatistics(){
        Statistics statistics = statisticsService.getTotalGoalStatistics();
        Response<Statistics> response = new Response<>("전체 목표 통계 조회에 성공했습니다.",statistics);
        return ResponseEntity.ok(response);
    }
    @GetMapping("/user")
    public ResponseEntity<Response<Statistics>> getUserGoalStatistics(@RequestHeader("Authorization") String accessToken){
        long userId = credentialService.getUserId(accessToken);
        Statistics statistics = statisticsService.getGoalStatisticsByUserId(userId);
        Response<Statistics> response = new Response<>("유저 목표 통계 조회에 성공했습니다.",statistics);
        return ResponseEntity.ok(response);
    }
    @GetMapping("/user/points")
    public ResponseEntity<Response<PointState>> getUserPoints(@RequestHeader("Authorization") String accessToken){
        long userId = credentialService.getUserId(accessToken);
        PointState pointState = statisticsService.getPointStateByUserId(userId);
        Response<PointState> response = new Response<>("포인트 조회에 성공했습니다.",pointState);
        return ResponseEntity.ok(response);
    }
}

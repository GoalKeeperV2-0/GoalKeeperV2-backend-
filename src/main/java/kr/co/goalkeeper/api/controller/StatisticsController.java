package kr.co.goalkeeper.api.controller;

import kr.co.goalkeeper.api.model.response.Response;
import kr.co.goalkeeper.api.model.response.Statistics;
import kr.co.goalkeeper.api.service.port.StatisticsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/statistics")
public class StatisticsController {
    private final StatisticsService statisticsService;

    public StatisticsController(StatisticsService statisticsService) {
        this.statisticsService = statisticsService;
    }

    @GetMapping("/total")
    public ResponseEntity<Response<Statistics>> getTotalStatistics(){
        Statistics statistics = statisticsService.getTotalStatistics();
        Response<Statistics> response = new Response<>("전체 통계 조회에 성공했습니다.",statistics);
        return ResponseEntity.ok(response);
    }
}

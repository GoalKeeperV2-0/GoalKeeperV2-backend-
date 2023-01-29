package kr.co.goalkeeper.api.controller;

import kr.co.goalkeeper.api.model.entity.CategoryType;
import kr.co.goalkeeper.api.model.entity.ManyTimeGoal;
import kr.co.goalkeeper.api.model.entity.OneTimeGoal;
import kr.co.goalkeeper.api.model.entity.User;
import kr.co.goalkeeper.api.model.request.ManyTimeGoalRequest;
import kr.co.goalkeeper.api.model.request.OneTimeGoalRequest;
import kr.co.goalkeeper.api.model.response.ManyTimeCertificationResponse;
import kr.co.goalkeeper.api.model.response.ManyTimeGoalResponse;
import kr.co.goalkeeper.api.model.response.OneTimeGoalResponse;
import kr.co.goalkeeper.api.model.response.Response;
import kr.co.goalkeeper.api.service.*;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/goal")
public class GoalController {
   private ManyTimeCertificationService manyTimeCertificationService;
   private OneTimeCertificationService oneTimeCertificationService;
   private OneTimeGoalService oneTimeGoalService;
   private ManyTimeGoalService manyTimeGoalService;
   private GoalKeeperTokenService goalKeeperTokenService;
   private UserService userService;

    public GoalController(ManyTimeCertificationService manyTimeCertificationService, OneTimeCertificationService oneTimeCertificationService, OneTimeGoalService oneTimeGoalService, ManyTimeGoalService manyTimeGoalService, GoalKeeperTokenService goalKeeperTokenService, UserService userService) {
        this.manyTimeCertificationService = manyTimeCertificationService;
        this.oneTimeCertificationService = oneTimeCertificationService;
        this.oneTimeGoalService = oneTimeGoalService;
        this.manyTimeGoalService = manyTimeGoalService;
        this.goalKeeperTokenService = goalKeeperTokenService;
        this.userService = userService;
    }

    @PostMapping("/onetime")
    public ResponseEntity<Response<?>> addOneTimeGoal(@RequestBody OneTimeGoalRequest oneTimeGoalRequest, @RequestHeader("Authorization") String accessToken){
        long userId = goalKeeperTokenService.getUserId(accessToken);
        User user = userService.getUserById(userId);
        OneTimeGoal oneTimeGoal = new OneTimeGoal(oneTimeGoalRequest,user);
        long goalId = oneTimeGoalService.createOneTimeGoal(oneTimeGoal);
        OneTimeGoalResponse result = OneTimeGoalResponse.builder()
                .id(goalId)
                .content(oneTimeGoal.getContent())
                .title(oneTimeGoal.getTitle())
                .endDate(oneTimeGoal.getEndDate())
                .categoryType(oneTimeGoalRequest.getCategoryType())
                .reward(oneTimeGoal.getReward())
                .point(oneTimeGoal.getPoint())
                .build();
        Response<OneTimeGoalResponse> response = new Response<>("일반목표 등록에 성공했습니다.",result);
        return ResponseEntity.ok(response);
    }
    @PostMapping("/manytime")
    public ResponseEntity<Response<?>> addManyTimeGoal(@RequestBody ManyTimeGoalRequest manyTimeGoalRequest, @RequestHeader("Authorization") String accessToken){
        long userId = goalKeeperTokenService.getUserId(accessToken);
        User user = userService.getUserById(userId);
        ManyTimeGoal manyTimeGoal = new ManyTimeGoal(manyTimeGoalRequest,user);
        long goalId = manyTimeGoalService.createManyTimeGoal(manyTimeGoal);
        ManyTimeGoalResponse result = ManyTimeGoalResponse.builder()
                .id(goalId)
                .content(manyTimeGoal.getContent())
                .title(manyTimeGoal.getTitle())
                .startDate(manyTimeGoal.getStartDate())
                .endDate(manyTimeGoal.getEndDate())
                .categoryType(manyTimeGoalRequest.getCategoryType())
                .reward(manyTimeGoal.getReward())
                .point(manyTimeGoal.getPoint())
                .certDates(manyTimeGoalRequest.getCertDates())
                .build();
        Response<ManyTimeGoalResponse> response = new Response<>("지속목표 등록에 성공했습니다.",result);
        return ResponseEntity.ok(response);
    }
    @GetMapping("/manytime/{category:[A-Z]+}/certifications")
    public ResponseEntity<Response<Page<ManyTimeCertificationResponse>>> getManyTimeCertificationByCategory(@PathVariable("category")CategoryType categoryType,@RequestParam int page){
        Page<ManyTimeCertificationResponse> result = manyTimeCertificationService.getManyTimeCertificationsByCategory(categoryType,page).map(ManyTimeCertificationResponse::new);
        Response<Page<ManyTimeCertificationResponse>> response = new Response<>("카테고리별 지속목표 인증 조회에 성공했습니다.",result);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/manytime/{goalId:[0-9]+}/certifications")
    public ResponseEntity<Response<Page<ManyTimeCertificationResponse>>> getManyTimeCertificationByGoalId(@PathVariable("goalId")long goalId,@RequestParam int page){
        Page<ManyTimeCertificationResponse> result = manyTimeCertificationService.getCertificationsByGoalId(goalId,page).map(ManyTimeCertificationResponse::new);
        Response<Page<ManyTimeCertificationResponse>> response = new Response<>("카테고리별 지속목표 인증 조회에 성공했습니다.",result);
        return ResponseEntity.ok(response);
    }
}

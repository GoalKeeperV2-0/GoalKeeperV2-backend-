package kr.co.goalkeeper.api.controller;

import kr.co.goalkeeper.api.model.entity.*;
import kr.co.goalkeeper.api.model.request.ManyTimeCertificationRequest;
import kr.co.goalkeeper.api.model.request.ManyTimeGoalRequest;
import kr.co.goalkeeper.api.model.response.*;
import kr.co.goalkeeper.api.service.*;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/goal/manytime")
public class GoalController {
   private final ManyTimeCertificationService manyTimeCertificationService;
   private final ManyTimeGoalService manyTimeGoalService;
   private final GoalKeeperTokenService goalKeeperTokenService;
   private final UserService userService;

    public GoalController(ManyTimeCertificationService manyTimeCertificationService, ManyTimeGoalService manyTimeGoalService, GoalKeeperTokenService goalKeeperTokenService, UserService userService) {
        this.manyTimeCertificationService = manyTimeCertificationService;
        this.manyTimeGoalService = manyTimeGoalService;
        this.goalKeeperTokenService = goalKeeperTokenService;
        this.userService = userService;
    }

    @PostMapping("")
    public ResponseEntity<Response<?>> addManyTimeGoal(@RequestBody ManyTimeGoalRequest manyTimeGoalRequest, @RequestHeader("Authorization") String accessToken){
        long userId = goalKeeperTokenService.getUserId(accessToken);
        User user = userService.getUserById(userId);
        ManyTimeGoal manyTimeGoal = new ManyTimeGoal(manyTimeGoalRequest,user);
        manyTimeGoal = manyTimeGoalService.createManyTimeGoal(manyTimeGoal);
        ManyTimeGoalResponse result = new ManyTimeGoalResponse(manyTimeGoal);
        Response<ManyTimeGoalResponse> response = new Response<>("지속목표 등록에 성공했습니다.",result);
        return ResponseEntity.ok(response);
    }
    @GetMapping("/{category:[A-Z]+}")
    public ResponseEntity<Response<?>> getManyTimeGoalsByCategoryAndUser(@PathVariable("category")CategoryType categoryType,@RequestHeader("Authorization") String accessToken,@RequestParam int page){
        long userId = goalKeeperTokenService.getUserId(accessToken);
        Page<ManyTimeGoalResponse> result = manyTimeGoalService.getManyTimeGoalsByUserIdAndCategory(userId,categoryType,page).map(ManyTimeGoalResponse::new);
        Response<Page<ManyTimeGoalResponse>> response = new Response<>("자신이 등록한 지속 목표 조회에 성공했습니다.",result);
        return ResponseEntity.ok(response);
    }
    @GetMapping("/{category:[A-Z]+}/certifications")
    public ResponseEntity<Response<Page<ManyTimeCertificationResponse>>> getManyTimeCertificationByCategory(@PathVariable("category")CategoryType categoryType,@RequestParam int page){
        Page<ManyTimeCertificationResponse> result = manyTimeCertificationService.getManyTimeCertificationsByCategory(categoryType,page).map(ManyTimeCertificationResponse::new);
        Response<Page<ManyTimeCertificationResponse>> response = new Response<>("카테고리별 지속목표 인증 조회에 성공했습니다.",result);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{goalId:[0-9]+}/certifications")
    public ResponseEntity<Response<Page<ManyTimeCertificationResponse>>> getManyTimeCertificationByGoalId(@PathVariable("goalId")long goalId,@RequestParam int page){
        Page<ManyTimeCertificationResponse> result = manyTimeCertificationService.getCertificationsByGoalId(goalId,page).map(ManyTimeCertificationResponse::new);
        Response<Page<ManyTimeCertificationResponse>> response = new Response<>("목표 ID별 지속목표 인증 조회에 성공했습니다.",result);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{goalId:[0-9]+}/certification")
    public ResponseEntity<Response<?>> createManyTimeCertificationByGoalId(@PathVariable("goalId")long goalId, @RequestBody ManyTimeCertificationRequest dto,@RequestHeader("Authorization") String accessToken){
        dto.fixGoalId(goalId);
        ManyTimeGoal manyTimeGoal = manyTimeGoalService.getManyTimeGoalById(goalId);
        ManyTimeCertification manyTimeCertification = new ManyTimeCertification(dto);
        manyTimeCertification.setManyTimeGoal(manyTimeGoal);
        long userId = goalKeeperTokenService.getUserId(accessToken);
        ManyTimeCertificationResponse result = new ManyTimeCertificationResponse(manyTimeCertificationService.createCertification(manyTimeCertification,userId));
        Response<ManyTimeCertificationResponse> response = new Response<>("인증 등록에 성공했습니다.",result);
        return ResponseEntity.ok(response);
    }
}

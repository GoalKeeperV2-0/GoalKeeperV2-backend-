package kr.co.goalkeeper.api.controller;

import kr.co.goalkeeper.api.model.entity.CategoryType;
import kr.co.goalkeeper.api.model.entity.ManyTimeGoal;
import kr.co.goalkeeper.api.model.entity.OneTimeGoal;
import kr.co.goalkeeper.api.model.entity.User;
import kr.co.goalkeeper.api.model.request.ManyTimeGoalRequest;
import kr.co.goalkeeper.api.model.request.OneTimeGoalRequest;
import kr.co.goalkeeper.api.model.response.*;
import kr.co.goalkeeper.api.service.*;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/goal")
public class GoalController {
   private final ManyTimeCertificationService manyTimeCertificationService;
   private final OneTimeCertificationService oneTimeCertificationService;
   private final OneTimeGoalService oneTimeGoalService;
   private final ManyTimeGoalService manyTimeGoalService;
   private final GoalKeeperTokenService goalKeeperTokenService;
   private final UserService userService;

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
    @GetMapping("/onetime/{category:[A-Z]+}")
    public ResponseEntity<Response<?>> getOneTimeGoalsByCategoryAndUser(@PathVariable("category")CategoryType categoryType,@RequestHeader("Authorization") String accessToken,@RequestParam int page){
        long userId = goalKeeperTokenService.getUserId(accessToken);
        Page<OneTimeGoalResponse> result = oneTimeGoalService.getOneTimeGoalsByUserIdAndCategory(userId,categoryType,page).map(OneTimeGoalResponse::new);
        Response<Page<OneTimeGoalResponse>> response = new Response<>("자신이 등록한 일반 목표 조회에 성공했습니다.",result);
        return ResponseEntity.ok(response);
    }
    @GetMapping("/onetime/{category:[A-Z]+}/certifications")
    public ResponseEntity<Response<?>> getOneTimeCertificationByCategory(@PathVariable("category")CategoryType categoryType,@RequestParam int page){
        Page<OnetimeCertificationResponse> result = oneTimeCertificationService.getOneTimeCertificationsByCategory(categoryType,page).map(OnetimeCertificationResponse::new);
        Response<Page<OnetimeCertificationResponse>> response = new Response<>("카테고리별 일반목표 인증 조회에 성공했습니다.",result);
        return ResponseEntity.ok(response);
    }
    @GetMapping("/onetime/{goalId:[0-9]+}/certification")
    public ResponseEntity<Response<?>> getOneTimeCertificationByGoalId(@PathVariable("goalId")long goalId){
        OnetimeCertificationResponse result = new OnetimeCertificationResponse(oneTimeCertificationService.getCertificationByGoalId(goalId));
        Response<OnetimeCertificationResponse> response = new Response<>("목표 ID별 일반목표 인증 조회에 성공했습니다.",result);
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
    @GetMapping("/manytime/{category:[A-Z]+}")
    public ResponseEntity<Response<?>> getManyTimeGoalsByCategoryAndUser(@PathVariable("category")CategoryType categoryType,@RequestHeader("Authorization") String accessToken,@RequestParam int page){
        long userId = goalKeeperTokenService.getUserId(accessToken);
        Page<ManyTimeGoalResponse> result = manyTimeGoalService.getManyTimeGoalsByUserIdAndCategory(userId,categoryType,page).map(ManyTimeGoalResponse::new);
        Response<Page<ManyTimeGoalResponse>> response = new Response<>("자신이 등록한 지속 목표 조회에 성공했습니다.",result);
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
        Response<Page<ManyTimeCertificationResponse>> response = new Response<>("목표 ID별 지속목표 인증 조회에 성공했습니다.",result);
        return ResponseEntity.ok(response);
    }
}

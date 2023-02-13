package kr.co.goalkeeper.api.controller;

import kr.co.goalkeeper.api.model.entity.*;
import kr.co.goalkeeper.api.model.request.ManyTimeCertificationRequest;
import kr.co.goalkeeper.api.model.request.ManyTimeGoalRequest;
import kr.co.goalkeeper.api.model.request.OneTimeGoalRequest;
import kr.co.goalkeeper.api.model.request.OnetimeCertificationRequest;
import kr.co.goalkeeper.api.model.response.*;
import kr.co.goalkeeper.api.service.port.*;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.function.Function;

@RestController
@RequestMapping("api/goal")
public class GoalController {
    private final ManyTimeCertificationService manyTimeCertificationService;
    private final ManyTimeGoalService manyTimeGoalService;
    private final OneTimeCertificationService oneTimeCertificationService;
    private final OneTimeGoalService oneTimeGoalService;
    private final GoalGetService goalGetService;
    private final CredentialService credentialService;

    private final Function<? super Goal, ? extends GoalResponse> mapper = goal -> {
        if(goal instanceof ManyTimeGoal){
            return new ManyTimeGoalResponse((ManyTimeGoal) goal);
        }else{
            return new OneTimeGoalResponse((OneTimeGoal) goal);
        }
    };

    public GoalController(ManyTimeCertificationService manyTimeCertificationService, ManyTimeGoalService manyTimeGoalService, OneTimeCertificationService oneTimeCertificationService, OneTimeGoalService oneTimeGoalService, GoalGetService goalGetService, CredentialService credentialService) {
        this.manyTimeCertificationService = manyTimeCertificationService;
        this.manyTimeGoalService = manyTimeGoalService;
        this.oneTimeCertificationService = oneTimeCertificationService;
        this.oneTimeGoalService = oneTimeGoalService;
        this.goalGetService = goalGetService;
        this.credentialService = credentialService;
    }

    @PostMapping("/manyTime")
    public ResponseEntity<Response<?>> addManyTimeGoal(@RequestBody ManyTimeGoalRequest manyTimeGoalRequest, @RequestHeader("Authorization") String accessToken){
        long userId = credentialService.getUserId(accessToken);
        User user = credentialService.getUserById(userId);
        ManyTimeGoal manyTimeGoal = new ManyTimeGoal(manyTimeGoalRequest,user);
        manyTimeGoal = manyTimeGoalService.createManyTimeGoal(manyTimeGoal);
        ManyTimeGoalResponse result = new ManyTimeGoalResponse(manyTimeGoal);
        Response<ManyTimeGoalResponse> response = new Response<>("지속목표 등록에 성공했습니다.",result);
        return ResponseEntity.ok(response);
    }

    @GetMapping("")
    public ResponseEntity<Response<Page<GoalResponse>>> getGoalsByUser(@RequestHeader("Authorization") String accessToken,@RequestParam int page){
        long userId = credentialService.getUserId(accessToken);
        Page<GoalResponse> result = goalGetService.getGoalsByUserId(userId,page).map(mapper);
        Response<Page<GoalResponse>> response = new Response<>("자신이 등록한 목표 조회에 성공했습니다.",result);
        return ResponseEntity.ok(response);
    }
    @GetMapping("/{category:[A-Z]+}")
    public ResponseEntity<Response<Page<GoalResponse>>> getGoalsByCategoryAndUser(@PathVariable("category")CategoryType categoryType, @RequestHeader("Authorization") String accessToken, @RequestParam int page){
        long userId = credentialService.getUserId(accessToken);
        Page<GoalResponse> result = goalGetService.getGoalsByUserIdAndCategory(userId,categoryType,page).map(mapper);
        Response<Page<GoalResponse>> response = new Response<>("자신이 등록한 목표 조회에 성공했습니다.",result);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/manyTime/{goalId:[0-9]+}/certification")
    public ResponseEntity<Response<?>> createManyTimeCertificationByGoalId(@PathVariable("goalId")long goalId, @RequestBody ManyTimeCertificationRequest dto,@RequestHeader("Authorization") String accessToken){
        dto.fixGoalId(goalId);
        Goal goal = goalGetService.getGoalById(goalId);
        ManyTimeCertification manyTimeCertification = new ManyTimeCertification(dto);
        manyTimeCertification.setGoal(goal);
        long userId = credentialService.getUserId(accessToken);
        ManyTimeCertificationResponse result = new ManyTimeCertificationResponse(manyTimeCertificationService.createCertification(manyTimeCertification,userId));
        Response<ManyTimeCertificationResponse> response = new Response<>("인증 등록에 성공했습니다.",result);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/oneTime")
    public ResponseEntity<Response<?>> addOneTimeGoal(@RequestBody OneTimeGoalRequest oneTimeGoalRequest, @RequestHeader("Authorization") String accessToken){
        long userId = credentialService.getUserId(accessToken);
        User user = credentialService.getUserById(userId);
        OneTimeGoal oneTimeGoal = new OneTimeGoal(oneTimeGoalRequest,user);
        oneTimeGoal = oneTimeGoalService.createOneTimeGoal(oneTimeGoal);
        OneTimeGoalResponse result = new OneTimeGoalResponse(oneTimeGoal);
        Response<OneTimeGoalResponse> response = new Response<>("일반목표 등록에 성공했습니다.",result);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/oneTime/{goalId:[0-9]+}/certification")
    public ResponseEntity<Response<?>> createOneTimeCertificationByGoalId(@PathVariable("goalId")long goalId, @RequestBody OnetimeCertificationRequest dto, @RequestHeader("Authorization") String accessToken){
        dto.fixGoalId(goalId);
        OneTimeCertification oneTimeCertification = new OneTimeCertification(dto);
        Goal goal = goalGetService.getGoalById(goalId);
        oneTimeCertification.setGoal(goal);
        long userId = credentialService.getUserId(accessToken);
        OneTimeCertificationResponse result = new OneTimeCertificationResponse(oneTimeCertificationService.createCertification(oneTimeCertification,userId));
        Response<OneTimeCertificationResponse> response = new Response<>("인증 등록에 성공했습니다.",result);
        return ResponseEntity.ok(response);
    }
}

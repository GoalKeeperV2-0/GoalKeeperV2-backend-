package kr.co.goalkeeper.api.controller;

import kr.co.goalkeeper.api.model.entity.*;
import kr.co.goalkeeper.api.model.request.ManyTimeGoalRequest;
import kr.co.goalkeeper.api.model.request.OneTimeGoalRequest;
import kr.co.goalkeeper.api.model.response.*;
import kr.co.goalkeeper.api.service.port.*;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.function.Function;

@RestController
@RequestMapping("api/goal")
@AllArgsConstructor
public class GoalController {
    private final ManyTimeGoalService manyTimeGoalService;
    private final OneTimeGoalService oneTimeGoalService;
    private final GoalGetService goalGetService;
    private final CredentialService credentialService;
    private final HoldGoalService holdGoalService;

    private final Function<? super Goal, ? extends GoalResponse> mapper = goal -> {
        if(goal instanceof ManyTimeGoal){
            return new ManyTimeGoalResponse((ManyTimeGoal) goal);
        }else{
            return new OneTimeGoalResponse((OneTimeGoal) goal);
        }
    };

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
        Page<GoalResponse> result = goalGetService.getGoalsByUserId(userId,page);
        Response<Page<GoalResponse>> response = new Response<>("자신이 등록한 목표 조회에 성공했습니다.",result);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{category:[A-Z]+}")
    public ResponseEntity<Response<Page<GoalResponse>>> getGoalsByCategoryAndUser(@PathVariable("category")CategoryType categoryType, @RequestHeader("Authorization") String accessToken, @RequestParam int page){
        long userId = credentialService.getUserId(accessToken);
        Page<GoalResponse> result = goalGetService.getGoalsByUserIdAndCategory(userId,categoryType,page);
        Response<Page<GoalResponse>> response = new Response<>("자신이 등록한 목표 조회에 성공했습니다.",result);
        return ResponseEntity.ok(response);
    }
    @GetMapping("/{goalId:[0-9]+}")
    public ResponseEntity<Response<GoalResponse>> getGoalByGoalId(@PathVariable long goalId, @RequestHeader("Authorization") String accessToken){
        long userId = credentialService.getUserId(accessToken);
        Goal goal = goalGetService.getMyGoalById(userId,goalId);
        GoalResponse goalResponse = mapper.apply(goal);
        Response<GoalResponse> response = new Response<>("자신이 등록한 목표 조회에 성공했습니다.",goalResponse);
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
    @PatchMapping("/hold/{goalId:[0-9]+}")
    public ResponseEntity<Response<String>> holdGoal(@PathVariable long goalId, @RequestHeader("Authorization") String accessToken){
        long userId = credentialService.getUserId(accessToken);
        User user = credentialService.getUserById(userId);
        holdGoalService.holdGoal(user,goalId);
        Response<String> response = new Response<>("검토 요청에 성공했습니다.","");
        return ResponseEntity.ok(response);
    }
}

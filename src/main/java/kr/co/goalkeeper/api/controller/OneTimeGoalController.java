package kr.co.goalkeeper.api.controller;

import kr.co.goalkeeper.api.model.entity.CategoryType;
import kr.co.goalkeeper.api.model.entity.OneTimeCertification;
import kr.co.goalkeeper.api.model.entity.OneTimeGoal;
import kr.co.goalkeeper.api.model.entity.User;
import kr.co.goalkeeper.api.model.request.OneTimeGoalRequest;
import kr.co.goalkeeper.api.model.request.OnetimeCertificationRequest;
import kr.co.goalkeeper.api.model.response.OneTimeCertificationResponse;
import kr.co.goalkeeper.api.model.response.OneTimeGoalResponse;
import kr.co.goalkeeper.api.model.response.Response;
import kr.co.goalkeeper.api.service.port.CredentialService;
import kr.co.goalkeeper.api.service.port.OneTimeCertificationService;
import kr.co.goalkeeper.api.service.port.OneTimeGoalService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
@RestController
@RequestMapping("api/goal/onetime")
public class OneTimeGoalController {
    private final OneTimeCertificationService oneTimeCertificationService;
    private final OneTimeGoalService oneTimeGoalService;
    private final CredentialService credentialService;

    public OneTimeGoalController(OneTimeCertificationService oneTimeCertificationService, OneTimeGoalService oneTimeGoalService, CredentialService credentialService) {
        this.oneTimeCertificationService = oneTimeCertificationService;
        this.oneTimeGoalService = oneTimeGoalService;
        this.credentialService = credentialService;
    }

    @PostMapping("")
    public ResponseEntity<Response<?>> addOneTimeGoal(@RequestBody OneTimeGoalRequest oneTimeGoalRequest, @RequestHeader("Authorization") String accessToken){
        long userId = credentialService.getUserId(accessToken);
        User user = credentialService.getUserById(userId);
        OneTimeGoal oneTimeGoal = new OneTimeGoal(oneTimeGoalRequest,user);
        oneTimeGoal = oneTimeGoalService.createOneTimeGoal(oneTimeGoal);
        OneTimeGoalResponse result = new OneTimeGoalResponse(oneTimeGoal);
        Response<OneTimeGoalResponse> response = new Response<>("일반목표 등록에 성공했습니다.",result);
        return ResponseEntity.ok(response);
    }
    @GetMapping("")
    public ResponseEntity<Response<?>> getOneTimeGoalsByUser(@RequestHeader("Authorization") String accessToken, @RequestParam int page){
        long userId = credentialService.getUserId(accessToken);
        Page<OneTimeGoalResponse> result = oneTimeGoalService.getOneTimeGoalsByUserId(userId,page).map(OneTimeGoalResponse::new);
        Response<Page<OneTimeGoalResponse>> response = new Response<>("자신이 등록한 일반 목표 조회에 성공했습니다.",result);
        return ResponseEntity.ok(response);
    }
    @GetMapping("/{category:[A-Z]+}")
    public ResponseEntity<Response<?>> getOneTimeGoalsByCategoryAndUser(@PathVariable("category") CategoryType categoryType, @RequestHeader("Authorization") String accessToken, @RequestParam int page){
        long userId = credentialService.getUserId(accessToken);
        Page<OneTimeGoalResponse> result = oneTimeGoalService.getOneTimeGoalsByUserIdAndCategory(userId,categoryType,page).map(OneTimeGoalResponse::new);
        Response<Page<OneTimeGoalResponse>> response = new Response<>("자신이 등록한 일반 목표 조회에 성공했습니다.",result);
        return ResponseEntity.ok(response);
    }
    @GetMapping("/{category:[A-Z]+}/certifications")
    public ResponseEntity<Response<?>> getOneTimeCertificationByCategory(@PathVariable("category")CategoryType categoryType,@RequestParam int page){
        Page<OneTimeCertificationResponse> result = oneTimeCertificationService.getOneTimeCertificationsByCategory(categoryType,page).map(OneTimeCertificationResponse::new);
        Response<Page<OneTimeCertificationResponse>> response = new Response<>("카테고리별 일반목표 인증 조회에 성공했습니다.",result);
        return ResponseEntity.ok(response);
    }
    @GetMapping("/certifications")
    public ResponseEntity<Response<?>> getOneTimeCertification(@RequestParam int page){
        Page<OneTimeCertificationResponse> result = oneTimeCertificationService.getOneTimeCertifications(page).map(OneTimeCertificationResponse::new);
        Response<Page<OneTimeCertificationResponse>> response = new Response<>("카테고리별 일반목표 인증 조회에 성공했습니다.",result);
        return ResponseEntity.ok(response);
    }
    @GetMapping("/{goalId:[0-9]+}/certification")
    public ResponseEntity<Response<?>> getOneTimeCertificationByGoalId(@PathVariable("goalId")long goalId){
        OneTimeCertificationResponse result = new OneTimeCertificationResponse(oneTimeCertificationService.getCertificationByGoalId(goalId));
        Response<OneTimeCertificationResponse> response = new Response<>("목표 ID별 일반목표 인증 조회에 성공했습니다.",result);
        return ResponseEntity.ok(response);
    }
    @PostMapping("/{goalId:[0-9]+}/certification")
    public ResponseEntity<Response<?>> createOneTimeCertificationByGoalId(@PathVariable("goalId")long goalId, @RequestBody OnetimeCertificationRequest dto, @RequestHeader("Authorization") String accessToken){
        dto.fixGoalId(goalId);
        OneTimeCertification oneTimeCertification = new OneTimeCertification(dto);
        OneTimeGoal oneTimeGoal = oneTimeGoalService.getOneTimeGoalById(goalId);
        oneTimeCertification.setGoal(oneTimeGoal);
        long userId = credentialService.getUserId(accessToken);
        OneTimeCertificationResponse result = new OneTimeCertificationResponse(oneTimeCertificationService.createCertification(oneTimeCertification,userId));
        Response<OneTimeCertificationResponse> response = new Response<>("인증 등록에 성공했습니다.",result);
        return ResponseEntity.ok(response);
    }
}

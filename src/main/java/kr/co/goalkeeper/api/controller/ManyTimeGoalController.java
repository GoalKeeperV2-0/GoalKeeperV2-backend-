package kr.co.goalkeeper.api.controller;

import kr.co.goalkeeper.api.model.entity.CategoryType;
import kr.co.goalkeeper.api.model.entity.ManyTimeCertification;
import kr.co.goalkeeper.api.model.entity.ManyTimeGoal;
import kr.co.goalkeeper.api.model.entity.User;
import kr.co.goalkeeper.api.model.request.ManyTimeCertificationRequest;
import kr.co.goalkeeper.api.model.request.ManyTimeGoalRequest;
import kr.co.goalkeeper.api.model.response.ManyTimeCertificationResponse;
import kr.co.goalkeeper.api.model.response.ManyTimeGoalResponse;
import kr.co.goalkeeper.api.model.response.Response;
import kr.co.goalkeeper.api.service.port.CredentialService;
import kr.co.goalkeeper.api.service.port.ManyTimeCertificationService;
import kr.co.goalkeeper.api.service.port.ManyTimeGoalService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/goal/manytime")
public class ManyTimeGoalController {
   private final ManyTimeCertificationService manyTimeCertificationService;
   private final ManyTimeGoalService manyTimeGoalService;
   private final CredentialService credentialService;

    public ManyTimeGoalController(ManyTimeCertificationService manyTimeCertificationService, ManyTimeGoalService manyTimeGoalService, CredentialService credentialService) {
        this.manyTimeCertificationService = manyTimeCertificationService;
        this.manyTimeGoalService = manyTimeGoalService;
        this.credentialService = credentialService;
    }

    @PostMapping("")
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
    public ResponseEntity<Response<?>> getManyTimeGoalsByUser(@RequestHeader("Authorization") String accessToken,@RequestParam int page){
        long userId = credentialService.getUserId(accessToken);
        Page<ManyTimeGoalResponse> result = manyTimeGoalService.getManyTimeGoalsByUserId(userId,page).map(ManyTimeGoalResponse::new);
        Response<Page<ManyTimeGoalResponse>> response = new Response<>("자신이 등록한 지속 목표 조회에 성공했습니다.",result);
        return ResponseEntity.ok(response);
    }
    @GetMapping("/{category:[A-Z]+}")
    public ResponseEntity<Response<?>> getManyTimeGoalsByCategoryAndUser(@PathVariable("category")CategoryType categoryType,@RequestHeader("Authorization") String accessToken,@RequestParam int page){
        long userId = credentialService.getUserId(accessToken);
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

    @GetMapping("certifications")
    public ResponseEntity<Response<Page<ManyTimeCertificationResponse>>> getManyTimeCertification(@RequestParam int page){
        Page<ManyTimeCertificationResponse> result = manyTimeCertificationService.getManyTimeCertifications(page).map(ManyTimeCertificationResponse::new);
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
        long userId = credentialService.getUserId(accessToken);
        ManyTimeCertificationResponse result = new ManyTimeCertificationResponse(manyTimeCertificationService.createCertification(manyTimeCertification,userId));
        Response<ManyTimeCertificationResponse> response = new Response<>("인증 등록에 성공했습니다.",result);
        return ResponseEntity.ok(response);
    }
}

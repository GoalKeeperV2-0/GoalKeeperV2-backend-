package kr.co.goalkeeper.api.controller;

import kr.co.goalkeeper.api.model.entity.*;
import kr.co.goalkeeper.api.model.request.ManyTimeCertificationRequest;
import kr.co.goalkeeper.api.model.request.OnetimeCertificationRequest;
import kr.co.goalkeeper.api.model.response.*;
import kr.co.goalkeeper.api.service.port.*;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/certifications")
@AllArgsConstructor
public class CertificationController {
    private final CertificationGetService certificationGetService;
    private final ManyTimeCertificationService manyTimeCertificationService;
    private final OneTimeCertificationService oneTimeCertificationService;
    private final GoalGetService goalGetService;
    private final CredentialService credentialService;
    
    @GetMapping("/{category:[A-Z]+}")
    public ResponseEntity<Response<CertificationPageResponse>> getCertificationByCategory(@RequestHeader("Authorization") String accessToken,@PathVariable("category") CategoryType categoryType, @RequestParam int page){
        long userId = credentialService.getUserId(accessToken);
        CertificationPageResponse result = certificationGetService.getCertificationsByCategory(categoryType,userId,page);
        Response<CertificationPageResponse> response = new Response<>("카테고리별 목표 인증 조회에 성공했습니다.",result);
        return ResponseEntity.ok(response);
    }

    @GetMapping("")
    public ResponseEntity<Response<CertificationPageResponse>> getCertification(@RequestHeader("Authorization") String accessToken,@RequestParam int page){
        long userId = credentialService.getUserId(accessToken);
        CertificationPageResponse result = certificationGetService.getCertifications(userId,page);
        Response<CertificationPageResponse> response = new Response<>("전체 목표 인증 조회에 성공했습니다.",result);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{goalId:[0-9]+}")
    public ResponseEntity<Response<CertificationPageResponse>> getCertificationByGoalId(@RequestHeader("Authorization") String accessToken, @PathVariable("goalId")long goalId, @RequestParam int page){
        long userId = credentialService.getUserId(accessToken);
        CertificationPageResponse result = certificationGetService.getCertificationsByGoalId(goalId,userId,page);
        Response<CertificationPageResponse> response = new Response<>("목표 ID별 목표 인증 조회에 성공했습니다.",result);
        return ResponseEntity.ok(response);
    }

    @PostMapping(value = "/oneTime/{goalId:[0-9]+}")
    public ResponseEntity<Response<OneTimeCertificationResponse>> createOneTimeCertificationByGoalId(@PathVariable("goalId")long goalId, @ModelAttribute OnetimeCertificationRequest dto, @RequestHeader("Authorization") String accessToken){
        dto.fixGoalId(goalId);
        Goal goal = goalGetService.getGoalById(goalId);
        OneTimeCertification oneTimeCertification = new OneTimeCertification(dto,goal);
        long userId = credentialService.getUserId(accessToken);
        OneTimeCertificationResponse result = new OneTimeCertificationResponse(oneTimeCertificationService.createCertification(oneTimeCertification,userId));
        Response<OneTimeCertificationResponse> response = new Response<>("인증 등록에 성공했습니다.",result);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/manyTime/{goalId:[0-9]+}")
    public ResponseEntity<Response<ManyTimeCertificationResponse>> createManyTimeCertificationByGoalId(@PathVariable("goalId")long goalId, @ModelAttribute ManyTimeCertificationRequest dto, @RequestHeader("Authorization") String accessToken){
        dto.fixGoalId(goalId);
        Goal goal = goalGetService.getGoalById(goalId);
        ManyTimeCertification manyTimeCertification = new ManyTimeCertification(dto,goal);
        long userId = credentialService.getUserId(accessToken);
        ManyTimeCertificationResponse result = ManyTimeCertificationResponse.getCreateCertificationResponse(manyTimeCertificationService.createCertification(manyTimeCertification,userId));
        Response<ManyTimeCertificationResponse> response = new Response<>("인증 등록에 성공했습니다.",result);
        return ResponseEntity.ok(response);
    }
}

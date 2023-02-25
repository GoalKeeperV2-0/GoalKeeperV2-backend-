package kr.co.goalkeeper.api.controller;

import kr.co.goalkeeper.api.model.entity.*;
import kr.co.goalkeeper.api.model.request.ManyTimeCertificationRequest;
import kr.co.goalkeeper.api.model.request.OnetimeCertificationRequest;
import kr.co.goalkeeper.api.model.response.CertificationResponse;
import kr.co.goalkeeper.api.model.response.ManyTimeCertificationResponse;
import kr.co.goalkeeper.api.model.response.OneTimeCertificationResponse;
import kr.co.goalkeeper.api.model.response.Response;
import kr.co.goalkeeper.api.service.port.*;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.function.Function;

@RestController
@RequestMapping("/api/certifications")
@AllArgsConstructor
public class CertificationController {
    private final CertificationGetService certificationGetService;
    private final ManyTimeCertificationService manyTimeCertificationService;
    private final OneTimeCertificationService oneTimeCertificationService;
    private final GoalGetService goalGetService;
    private final CredentialService credentialService;

    private final Function<? super Certification, ? extends CertificationResponse> mapper = certification -> {
        if(certification instanceof ManyTimeCertification){
            return new ManyTimeCertificationResponse((ManyTimeCertification) certification);
        }else{
            return new OneTimeCertificationResponse((OneTimeCertification) certification);
        }
    };

    @GetMapping("/{category:[A-Z]+}")
    public ResponseEntity<Response<Page<CertificationResponse>>> getCertificationByCategory(@PathVariable("category") CategoryType categoryType, @RequestParam int page){
        Page<CertificationResponse> result = certificationGetService.getCertificationsByCategory(categoryType,page).map(mapper);
        Response<Page<CertificationResponse>> response = new Response<>("카테고리별 목표 인증 조회에 성공했습니다.",result);
        return ResponseEntity.ok(response);
    }

    @GetMapping("")
    public ResponseEntity<Response<Page<CertificationResponse>>> getCertification(@RequestParam int page){
        Page<CertificationResponse> result = certificationGetService.getCertifications(page).map(mapper);
        Response<Page<CertificationResponse>> response = new Response<>("전체 목표 인증 조회에 성공했습니다.",result);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{goalId:[0-9]+}")
    public ResponseEntity<Response<Page<CertificationResponse>>> getCertificationByGoalId(@PathVariable("goalId")long goalId,@RequestParam int page){
        Page<CertificationResponse> result = certificationGetService.getCertificationsByGoalId(goalId,page).map(mapper);
        Response<Page<CertificationResponse>> response = new Response<>("목표 ID별 목표 인증 조회에 성공했습니다.",result);
        return ResponseEntity.ok(response);
    }

    @PostMapping(value = "/oneTime/{goalId:[0-9]+}")
    public ResponseEntity<Response<?>> createOneTimeCertificationByGoalId(@PathVariable("goalId")long goalId, @ModelAttribute OnetimeCertificationRequest dto, @RequestHeader("Authorization") String accessToken){
        dto.fixGoalId(goalId);
        Goal goal = goalGetService.getGoalById(goalId);
        OneTimeCertification oneTimeCertification = new OneTimeCertification(dto,goal);
        long userId = credentialService.getUserId(accessToken);
        OneTimeCertificationResponse result = new OneTimeCertificationResponse(oneTimeCertificationService.createCertification(oneTimeCertification,userId));
        Response<OneTimeCertificationResponse> response = new Response<>("인증 등록에 성공했습니다.",result);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/manyTime/{goalId:[0-9]+}")
    public ResponseEntity<Response<?>> createManyTimeCertificationByGoalId(@PathVariable("goalId")long goalId, @ModelAttribute ManyTimeCertificationRequest dto, @RequestHeader("Authorization") String accessToken){
        dto.fixGoalId(goalId);
        Goal goal = goalGetService.getGoalById(goalId);
        ManyTimeCertification manyTimeCertification = new ManyTimeCertification(dto,goal);
        long userId = credentialService.getUserId(accessToken);
        ManyTimeCertificationResponse result = new ManyTimeCertificationResponse(manyTimeCertificationService.createCertification(manyTimeCertification,userId));
        Response<ManyTimeCertificationResponse> response = new Response<>("인증 등록에 성공했습니다.",result);
        return ResponseEntity.ok(response);
    }
}

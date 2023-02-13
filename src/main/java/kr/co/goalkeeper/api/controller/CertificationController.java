package kr.co.goalkeeper.api.controller;

import kr.co.goalkeeper.api.model.entity.CategoryType;
import kr.co.goalkeeper.api.model.entity.Certification;
import kr.co.goalkeeper.api.model.entity.ManyTimeCertification;
import kr.co.goalkeeper.api.model.entity.OneTimeCertification;
import kr.co.goalkeeper.api.model.response.CertificationResponse;
import kr.co.goalkeeper.api.model.response.ManyTimeCertificationResponse;
import kr.co.goalkeeper.api.model.response.OneTimeCertificationResponse;
import kr.co.goalkeeper.api.model.response.Response;
import kr.co.goalkeeper.api.service.port.CertificationGetService;
import kr.co.goalkeeper.api.service.port.ManyTimeCertificationService;
import kr.co.goalkeeper.api.service.port.OneTimeCertificationService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.function.Function;

@RestController
@RequestMapping("/api/certification")
public class CertificationController {
    private final CertificationGetService certificationGetService;
    private final ManyTimeCertificationService manyTimeCertificationService;
    private final OneTimeCertificationService oneTimeCertificationService;

    private final Function<? super Certification, ? extends CertificationResponse> mapper = certification -> {
        if(certification instanceof ManyTimeCertification){
            return new ManyTimeCertificationResponse((ManyTimeCertification) certification);
        }else{
            return new OneTimeCertificationResponse((OneTimeCertification) certification);
        }
    };

    public CertificationController(CertificationGetService certificationGetService, ManyTimeCertificationService manyTimeCertificationService, OneTimeCertificationService oneTimeCertificationService) {
        this.certificationGetService = certificationGetService;
        this.manyTimeCertificationService = manyTimeCertificationService;
        this.oneTimeCertificationService = oneTimeCertificationService;
    }
    @GetMapping("/{category:[A-Z]+}/certifications")
    public ResponseEntity<Response<Page<CertificationResponse>>> getManyTimeCertificationByCategory(@PathVariable("category") CategoryType categoryType, @RequestParam int page){
        Page<CertificationResponse> result = certificationGetService.getCertificationsByCategory(categoryType,page).map(mapper);
        Response<Page<CertificationResponse>> response = new Response<>("카테고리별 목표 인증 조회에 성공했습니다.",result);
        return ResponseEntity.ok(response);
    }

    @GetMapping("certifications")
    public ResponseEntity<Response<Page<CertificationResponse>>> getManyTimeCertification(@RequestParam int page){
        Page<CertificationResponse> result = certificationGetService.getCertifications(page).map(mapper);
        Response<Page<CertificationResponse>> response = new Response<>("카테고리별 목표 인증 조회에 성공했습니다.",result);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{goalId:[0-9]+}/certifications")
    public ResponseEntity<Response<Page<CertificationResponse>>> getManyTimeCertificationByGoalId(@PathVariable("goalId")long goalId,@RequestParam int page){
        Page<CertificationResponse> result = certificationGetService.getCertificationsByGoalId(goalId,page).map(mapper);
        Response<Page<CertificationResponse>> response = new Response<>("목표 ID별 목표 인증 조회에 성공했습니다.",result);
        return ResponseEntity.ok(response);
    }

}

package kr.co.goalkeeper.api.controller;

import kr.co.goalkeeper.api.model.entity.Certification;
import kr.co.goalkeeper.api.model.entity.User;
import kr.co.goalkeeper.api.model.entity.Verification;
import kr.co.goalkeeper.api.model.request.VerificationRequest;
import kr.co.goalkeeper.api.model.response.GoalKeeperToken;
import kr.co.goalkeeper.api.model.response.Response;
import kr.co.goalkeeper.api.service.port.CertificationGetService;
import kr.co.goalkeeper.api.service.port.CredentialService;
import kr.co.goalkeeper.api.service.port.VerificationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/verification")
public class VerificationController {
    private final CredentialService credentialService;
    private final VerificationService verificationService;
    private final CertificationGetService certificationGetService;

    public VerificationController(CredentialService credentialService, VerificationService verificationService, CertificationGetService certificationGetService) {
        this.credentialService = credentialService;
        this.verificationService = verificationService;
        this.certificationGetService = certificationGetService;
    }

    @PostMapping("/{certificationId:[0-9]+}")
    public ResponseEntity<Response<?>> createVerification(@RequestBody VerificationRequest verificationRequest,@RequestHeader("Authorization") String accessToken){
        long userId = credentialService.getUserId(accessToken);
        Verification verification = verificationService.createVerification(verificationRequest,userId);
        Response<String> response = new Response<>("검증 등록이 되었습니다.","");
        return ResponseEntity.ok(response);
    }
}

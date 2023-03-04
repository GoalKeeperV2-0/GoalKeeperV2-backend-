package kr.co.goalkeeper.api.controller;

import kr.co.goalkeeper.api.model.request.VerificationRequest;
import kr.co.goalkeeper.api.model.response.Response;
import kr.co.goalkeeper.api.service.port.CredentialService;
import kr.co.goalkeeper.api.service.port.VerificationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/verification")
public class VerificationController {
    private final CredentialService credentialService;
    private final VerificationService verificationService;

    public VerificationController(CredentialService credentialService, VerificationService verificationService) {
        this.credentialService = credentialService;
        this.verificationService = verificationService;
    }

    @PostMapping("")
    public ResponseEntity<Response<?>> createVerification(@RequestBody VerificationRequest verificationRequest, @RequestHeader("Authorization") String accessToken){
        long userId = credentialService.getUserId(accessToken);
        verificationService.createVerification(verificationRequest,userId);
        Response<String> response = new Response<>("검증 등록이 되었습니다.","");
        return ResponseEntity.ok(response);
    }
}

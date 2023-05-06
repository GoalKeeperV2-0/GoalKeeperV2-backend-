package kr.co.goalkeeper.api.service.port;

import kr.co.goalkeeper.api.model.entity.goal.Verification;
import kr.co.goalkeeper.api.model.request.VerificationRequest;

public interface VerificationService {
    Verification createVerification(VerificationRequest verificationRequest,long userId);
}

package kr.co.goalkeeper.api.service.impl;

import kr.co.goalkeeper.api.exception.GoalkeeperException;
import kr.co.goalkeeper.api.model.entity.Certification;
import kr.co.goalkeeper.api.model.entity.CertificationState;
import kr.co.goalkeeper.api.model.entity.User;
import kr.co.goalkeeper.api.model.entity.Verification;
import kr.co.goalkeeper.api.model.request.VerificationRequest;
import kr.co.goalkeeper.api.model.response.ErrorMessage;
import kr.co.goalkeeper.api.repository.CertificationRepository;
import kr.co.goalkeeper.api.repository.UserRepository;
import kr.co.goalkeeper.api.repository.VerificationRepository;
import kr.co.goalkeeper.api.service.port.VerificationService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@Transactional
public class SimpleVerificationService implements VerificationService {
    private final VerificationRepository verificationRepository;
    private final CertificationRepository certificationRepository;
    private final UserRepository userRepository;

    public SimpleVerificationService(VerificationRepository verificationRepository, CertificationRepository certificationRepository, UserRepository userRepository) {
        this.verificationRepository = verificationRepository;
        this.certificationRepository = certificationRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Verification createVerification(VerificationRequest verificationRequest,long userId) {
        Certification certification = certificationRepository.findById(verificationRequest.getCertificationId()).orElseThrow(() -> {
            ErrorMessage errorMessage = new ErrorMessage(404,"없는 인증입니다.");
            return new GoalkeeperException(errorMessage);
        });
        if(validatePermission(certification,userId)){
            ErrorMessage errorMessage = new ErrorMessage(401,"자신의 인증은 검증할 수 없습니다.");
            throw new GoalkeeperException(errorMessage);
        }
        if(validateVerifiableCertification(certification)){
            ErrorMessage errorMessage = new ErrorMessage(409,"검증 기간이 지난 인증입니다.");
            throw new GoalkeeperException(errorMessage);
        }
        if(validateCertificationState(certification)){
            ErrorMessage errorMessage = new ErrorMessage(409,"이미 검증이 완료된 인증입니다.");
            throw new GoalkeeperException(errorMessage);
        }
        User user = userRepository.findById(userId).orElseThrow(() -> {
            ErrorMessage errorMessage = new ErrorMessage(404,"없는 유저입니다.");
            return new GoalkeeperException(errorMessage);
        });
        user.giveVerificationReward();
        Verification verification = new Verification(certification,user,verificationRequest.isState());
        return verificationRepository.save(verification);
    }
    private boolean validatePermission(Certification certification,long userId){
        return certification.getGoal().getUser().getId()==userId;
    }
    private boolean validateVerifiableCertification(Certification certification){
        LocalDate now = LocalDate.now();
        LocalDate limit = certification.getDate().plusDays(7);
        return limit.isAfter(now) || limit.isEqual(now);
    }
    private boolean validateCertificationState(Certification certification){
        return certification.getState() == CertificationState.ONGOING;
    }
}

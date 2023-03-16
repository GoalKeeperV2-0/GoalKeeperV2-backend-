package kr.co.goalkeeper.api.service.impl;

import kr.co.goalkeeper.api.exception.GoalkeeperException;
import kr.co.goalkeeper.api.model.entity.*;
import kr.co.goalkeeper.api.model.response.CertificationPageResponse;
import kr.co.goalkeeper.api.model.response.ErrorMessage;
import kr.co.goalkeeper.api.repository.CertificationRepository;
import kr.co.goalkeeper.api.repository.VerificationRepository;
import kr.co.goalkeeper.api.service.port.CertificationGetService;
import kr.co.goalkeeper.api.service.port.ManyTimeCertificationService;
import kr.co.goalkeeper.api.service.port.OneTimeCertificationService;
import kr.co.goalkeeper.api.util.ImageSaver;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static kr.co.goalkeeper.api.model.entity.CertificationState.*;


@Service
@Transactional
class CertificationService implements OneTimeCertificationService, ManyTimeCertificationService , CertificationGetService {
    @Value("${file-save-location}")
    private String pictureRootPath;
    private final CertificationRepository certificationRepository;
    private final VerificationRepository verificationRepository;

    public CertificationService(CertificationRepository certificationRepository, VerificationRepository verificationRepository) {
        this.certificationRepository = certificationRepository;
        this.verificationRepository = verificationRepository;
    }

    private PageRequest makePageRequest(int page){
        int PAGE_SIZE = 9;
        return PageRequest.of(page, PAGE_SIZE, Sort.by("id").descending());
    }
    public ManyTimeCertification createCertification(ManyTimeCertification certification, long userId) {
        if(validatePermission(certification,userId)){
            ErrorMessage errorMessage = new ErrorMessage(401,"자신이 작성한 목표의 인증만 등록할 수 있습니다.");
            throw new GoalkeeperException(errorMessage);
        }
        if(!validateCertDate(certification)){
            ErrorMessage errorMessage = new ErrorMessage(400,"오늘은 인증날이 아닙니다.");
            throw new GoalkeeperException(errorMessage);
        }
        if(validateAlreadyCert(certification)){
            ErrorMessage errorMessage = new ErrorMessage(400,"이미 인증이 등록된 인증날 입니다.");
            throw new GoalkeeperException(errorMessage);
        }
        certification.changeGoalStateToWait();
        ImageSaver.saveCertificationPicture(certification,pictureRootPath);
        return certificationRepository.save(certification);
    }
    private boolean validatePermission(Certification certification, long userId){
        User user = certification.getGoal().getUser();
        return userId != user.getId();
    }
    private boolean validateCertDate(ManyTimeCertification certification){
        List<ManyTimeGoalCertDate> certDateList = ((ManyTimeGoal)certification.getGoal()).getCertDates();
        return certDateList.stream().anyMatch((cert)-> cert.getCertDate().equals(certification.getDate()));
    }
    private boolean validateAlreadyCert(ManyTimeCertification certification){
        return certificationRepository.existsByDateAndGoal_Id(certification.getDate(), certification.getGoal().getId());
    }

    @Override
    public CertificationPageResponse getCertificationsByGoalId(long goalId, long userId, int page) {
        Page<Certification> certs = certificationRepository.findAllByGoal_Id(goalId,makePageRequest(page));
        return getCertificationPageResponse(userId, certs);
    }
    private CertificationPageResponse getCertificationPageResponse(long userId, Page<Certification> certs) {
        if(certs.isEmpty()){
            return new CertificationPageResponse(certs, Collections.emptyList());
        }
        List<Long> certIds = new ArrayList<>();
        certs.getContent().forEach(certification -> certIds.add(certification.getId()));
        List<Verification> verifies = verificationRepository.findAllByCertification_IdInAndUser_Id(certIds,userId);
        return new CertificationPageResponse(certs,verifies);
    }

    @Override
    public CertificationPageResponse getCertificationsByCategory(CategoryType categoryType,long userId,int page) {
        Page<Certification> certs = certificationRepository.findByGoal_Category_CategoryTypeAndStateAndGoal_User_IdNotLike(categoryType,ONGOING,userId,makePageRequest(page));
        return getCertificationPageResponse(userId, certs);
    }

    @Override
    public CertificationPageResponse getCertifications(long userId,int page) {
        Page<Certification> certs = certificationRepository.findByStateAndGoal_User_IdNotLike(ONGOING,userId,makePageRequest(page));
        return getCertificationPageResponse(userId, certs);
    }

    @Override
    public Certification getCertificationById(long certificationId) {
        return certificationRepository.findById(certificationId).orElseThrow(() -> {
            ErrorMessage errorMessage = new ErrorMessage(404,"없는 인증입니다.");
            return new GoalkeeperException(errorMessage);
        });
    }

    @Override
    public OneTimeCertification createCertification(OneTimeCertification certification,long userId) {
        if(validatePermission(certification,userId)){
            ErrorMessage errorMessage = new ErrorMessage(401,"자신이 작성한 목표의 인증만 등록할 수 있습니다.");
            throw new GoalkeeperException(errorMessage);
        }
        if(!validateUniqueCertification(certification)){
            ErrorMessage errorMessage = new ErrorMessage(409,"이미 인증이 등록된 목표입니다.");
            throw new GoalkeeperException(errorMessage);
        }
        if(!validateCertificationState(certification)){
            ErrorMessage errorMessage = new ErrorMessage(409,"현재 진행중인 목표만 등록할 수 있습니다.");
            throw new GoalkeeperException(errorMessage);
        }
        certification.changeGoalStateToWait();
        ImageSaver.saveCertificationPicture(certification,pictureRootPath);
        return certificationRepository.save(certification);
    }
    private boolean validateCertificationState(OneTimeCertification certification){
        GoalState state = certification.getGoal().getGoalState();
        return state==GoalState.ONGOING;
    }
    private boolean validateUniqueCertification(OneTimeCertification certification){
        Page<Certification> searchResult = certificationRepository.findAllByGoal_Id(certification.getGoal().getId(),makePageRequest(0));
        return searchResult.getContent().isEmpty();
    }
}

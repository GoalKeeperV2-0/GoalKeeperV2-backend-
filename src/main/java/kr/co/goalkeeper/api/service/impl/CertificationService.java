package kr.co.goalkeeper.api.service.impl;

import kr.co.goalkeeper.api.exception.GoalkeeperException;
import kr.co.goalkeeper.api.model.entity.*;
import kr.co.goalkeeper.api.model.response.ErrorMessage;
import kr.co.goalkeeper.api.repository.CertificationRepository;
import kr.co.goalkeeper.api.service.port.CertificationGetService;
import kr.co.goalkeeper.api.service.port.ManyTimeCertificationService;
import kr.co.goalkeeper.api.service.port.OneTimeCertificationService;
import kr.co.goalkeeper.api.util.ImageSaver;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;


@Service
@Transactional
class CertificationService implements OneTimeCertificationService, ManyTimeCertificationService , CertificationGetService {
    @Value("${file-save-location}")
    private String pictureRootPath;
    private final CertificationRepository certificationRepository;
    private PageRequest makePageRequest(int page){
        int PAGE_SIZE = 9;
        return PageRequest.of(page, PAGE_SIZE);
    }

    public CertificationService(CertificationRepository certificationRepository) {
        this.certificationRepository = certificationRepository;
    }

    public ManyTimeCertification createCertification(ManyTimeCertification certification, long userId) {
        if(!validatePermission(certification,userId)){
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
        ImageSaver.saveCertificationPicture(certification,pictureRootPath);
        return certificationRepository.save(certification);
    }
    private boolean validatePermission(Certification certification, long userId){
        User user = certification.getGoal().getUser();
        return userId == user.getId();
    }
    private boolean validateCertDate(ManyTimeCertification certification){
        List<ManyTimeGoalCertDate> certDateList = ((ManyTimeGoal)certification.getGoal()).getCertDates();
        return certDateList.stream().anyMatch((cert)-> cert.getCertDate().equals(certification.getDate()));
    }
    private boolean validateAlreadyCert(ManyTimeCertification certification){
        boolean b = certificationRepository.existsByDateAndGoal_Id(certification.getDate(), certification.getGoal().getId());
        return b;
    }

    @Override
    public Page<Certification> getCertificationsByGoalId(long goalId,int page) {
        return certificationRepository.findAllByGoal_Id(goalId, makePageRequest(page));
    }

    @Override
    public Page<Certification> getCertificationsByCategory(CategoryType categoryType,int page) {
        return certificationRepository.findByGoal_Category_CategoryTypeAndGoal_GoalState(categoryType,GoalState.ONGOING,makePageRequest(page));
    }

    @Override
    public Page<Certification> getCertifications(int page) {
        return certificationRepository.findByGoal_GoalState(GoalState.ONGOING,makePageRequest(page));
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
        if(!validatePermission(certification,userId)){
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

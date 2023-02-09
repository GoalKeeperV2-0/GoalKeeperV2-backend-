package kr.co.goalkeeper.api.service;

import kr.co.goalkeeper.api.exception.GoalkeeperException;
import kr.co.goalkeeper.api.model.entity.*;
import kr.co.goalkeeper.api.model.response.ErrorMessage;
import kr.co.goalkeeper.api.repository.ManyTimeCertificationRepository;
import kr.co.goalkeeper.api.repository.OneTimeCertificationRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class CertificationService implements OneTimeCertificationService,ManyTimeCertificationService{
    private final OneTimeCertificationRepository oneTimeCertificationRepository;
    private final ManyTimeCertificationRepository manyTimeCertificationRepository;
    private PageRequest makePageRequest(int page){
        int PAGE_SIZE = 9;
        return PageRequest.of(page, PAGE_SIZE);
    }

    public CertificationService(OneTimeCertificationRepository oneTimeCertificationRepository, ManyTimeCertificationRepository manyTimeCertificationRepository) {
        this.oneTimeCertificationRepository = oneTimeCertificationRepository;
        this.manyTimeCertificationRepository = manyTimeCertificationRepository;
    }

    public ManyTimeCertification createCertification(ManyTimeCertification certification,long userId) {
        if(!validatePermission(certification,userId)){
            ErrorMessage errorMessage = new ErrorMessage(401,"자신이 작성한 목표의 인증만 등록할 수 있습니다.");
            throw new GoalkeeperException(errorMessage);
        }
        if(!validateCertDate(certification)){
            ErrorMessage errorMessage = new ErrorMessage(400,"오늘은 인증날이 아닙니다.");
            throw new GoalkeeperException(errorMessage);
        }
        return manyTimeCertificationRepository.save(certification);
    }
    private boolean validatePermission(ManyTimeCertification certification, long userId){
        User user = certification.getManyTimeGoal().getUser();
        return userId == user.getId();
    }
    private boolean validateCertDate(ManyTimeCertification certification){
        List<ManyTimeGoalCertDate> certDateList = certification.getManyTimeGoal().getCertDates();
        return certDateList.stream().anyMatch((cert)-> cert.getCertDate().equals(certification.getDate()));
    }

    @Override
    public Page<ManyTimeCertification> getCertificationsByGoalId(long goalId,int page) {
        return manyTimeCertificationRepository.findByManyTimeGoal_Id(goalId, makePageRequest(page));
    }

    @Override
    public Page<ManyTimeCertification> getManyTimeCertificationsByCategory(CategoryType categoryType,int page) {
        return manyTimeCertificationRepository.findByManyTimeGoal_Category_CategoryTypeAndManyTimeGoal_GoalState(categoryType,GoalState.ONGOING,makePageRequest(page));
    }

    @Override
    public Page<ManyTimeCertification> getManyTimeCertifications(int page) {
        return manyTimeCertificationRepository.findByManyTimeGoal_GoalState(GoalState.ONGOING,makePageRequest(page));
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
        return oneTimeCertificationRepository.save(certification);
    }
    private boolean validatePermission(OneTimeCertification certification, long userId){
        User user = certification.getOneTimeGoal().getUser();
        return userId == user.getId();
    }
    private boolean validateCertificationState(OneTimeCertification certification){
        GoalState state = certification.getOneTimeGoal().getGoalState();
        return state==GoalState.ONGOING;
    }
    private boolean validateUniqueCertification(OneTimeCertification certification){
        OneTimeCertification searchResult = oneTimeCertificationRepository.findByOneTimeGoal_Id(certification.getOneTimeGoal().getId()).orElse(null);
        return searchResult==null;
    }
    @Override
    public OneTimeCertification getCertificationByGoalId(long goalId) {
        return oneTimeCertificationRepository.findByOneTimeGoal_Id(goalId).orElseThrow(() -> {
            ErrorMessage errorMessage = new ErrorMessage(404,"없는 목표입니다.");
            return new GoalkeeperException(errorMessage);
        });
    }

    @Override
    public Page<OneTimeCertification> getOneTimeCertificationsByCategory(CategoryType categoryType,int page) {
        return oneTimeCertificationRepository.findByOneTimeGoal_Category_CategoryTypeAndOneTimeGoal_GoalState(categoryType,GoalState.ONGOING,makePageRequest(page));
    }

    @Override
    public Page<OneTimeCertification> getOneTimeCertifications(int page) {
        return oneTimeCertificationRepository.findByOneTimeGoal_GoalState(GoalState.ONGOING,makePageRequest(page));
    }
}

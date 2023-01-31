package kr.co.goalkeeper.api.service;

import kr.co.goalkeeper.api.model.entity.CategoryType;
import kr.co.goalkeeper.api.model.entity.ManyTimeCertification;
import kr.co.goalkeeper.api.model.entity.OneTimeCertification;
import kr.co.goalkeeper.api.model.response.ManyTimeCertificationResponse;
import kr.co.goalkeeper.api.model.response.OnetimeCertificationResponse;
import kr.co.goalkeeper.api.repository.ManyTimeCertificationRepository;
import kr.co.goalkeeper.api.repository.OneTimeCertificationRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class CertificationService implements OneTimeCertificationService,ManyTimeCertificationService{
    private static int PAGE_SIZE = 9;
    private OneTimeCertificationRepository oneTimeCertificationRepository;
    private ManyTimeCertificationRepository manyTimeCertificationRepository;
    private PageRequest makePageRequest(int page){
        return PageRequest.of(page,PAGE_SIZE);
    }

    public CertificationService(OneTimeCertificationRepository oneTimeCertificationRepository, ManyTimeCertificationRepository manyTimeCertificationRepository) {
        this.oneTimeCertificationRepository = oneTimeCertificationRepository;
        this.manyTimeCertificationRepository = manyTimeCertificationRepository;
    }

    public ManyTimeCertificationResponse createCertification(ManyTimeCertification certification) {
        return new ManyTimeCertificationResponse(manyTimeCertificationRepository.save(certification));
    }

    @Override
    public Page<ManyTimeCertification> getCertificationsByGoalId(long goalId,int page) {
        return manyTimeCertificationRepository.findByManyTimeGoal_Id(goalId, makePageRequest(page));
    }

    @Override
    public Page<ManyTimeCertification> getManyTimeCertificationsByCategory(CategoryType categoryType,int page) {
        return manyTimeCertificationRepository.findByManyTimeGoal_Category_CategoryType(categoryType,makePageRequest(page));
    }

    @Override
    public OnetimeCertificationResponse createCertification(OneTimeCertification certification) {
        return new OnetimeCertificationResponse(oneTimeCertificationRepository.save(certification));
    }

    @Override
    public OneTimeCertification getCertificationByGoalId(long goalId) {
        return oneTimeCertificationRepository.findByOneTimeGoal_Id(goalId);
    }

    @Override
    public Page<OneTimeCertification> getOneTimeCertificationsByCategory(CategoryType categoryType,int page) {
        return oneTimeCertificationRepository.findByOneTimeGoal_Category_CategoryType(categoryType,makePageRequest(page));
    }

}

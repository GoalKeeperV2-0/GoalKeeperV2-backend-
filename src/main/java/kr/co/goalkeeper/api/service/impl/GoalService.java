package kr.co.goalkeeper.api.service.impl;

import kr.co.goalkeeper.api.exception.GoalkeeperException;
import kr.co.goalkeeper.api.model.entity.*;
import kr.co.goalkeeper.api.model.response.ErrorMessage;
import kr.co.goalkeeper.api.model.response.GoalResponse;
import kr.co.goalkeeper.api.model.response.ManyTimeGoalResponse;
import kr.co.goalkeeper.api.model.response.OneTimeGoalResponse;
import kr.co.goalkeeper.api.repository.CertificationRepository;
import kr.co.goalkeeper.api.repository.GoalRepository;
import kr.co.goalkeeper.api.repository.ManyTimeGoalCertDateRepository;
import kr.co.goalkeeper.api.service.port.GoalGetService;
import kr.co.goalkeeper.api.service.port.HoldGoalService;
import kr.co.goalkeeper.api.service.port.ManyTimeGoalService;
import kr.co.goalkeeper.api.service.port.OneTimeGoalService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
class GoalService implements OneTimeGoalService, ManyTimeGoalService , GoalGetService, HoldGoalService {

    private final GoalRepository goalRepository;
    private final CertificationRepository certificationRepository;
    private final ManyTimeGoalCertDateRepository certDateRepository;
    private PageRequest makePageRequest(int page){
        int PAGE_SIZE = 8;
        return PageRequest.of(page, PAGE_SIZE, Sort.by("id").descending());
    }

    public GoalService(GoalRepository goalRepository, CertificationRepository certificationRepository, ManyTimeGoalCertDateRepository certDateRepository) {
        this.goalRepository = goalRepository;
        this.certificationRepository = certificationRepository;
        this.certDateRepository = certDateRepository;
    }

    @Override
    public ManyTimeGoal createManyTimeGoal(ManyTimeGoal manyTimeGoal) {
        if(!validateStartDateAndEndDate(manyTimeGoal)) {
            ErrorMessage errorMessage = new ErrorMessage(400,"목표 시작날짜는 목표 종료날짜보다 4일 이상 빨라야 합니다." +
                    "\n목표 시작날짜와 목표 종료날짜는 오늘보다 빠르면 안됩니다.");
            throw new GoalkeeperException(errorMessage);
        }
        if(!validatePoint(manyTimeGoal)){
            ErrorMessage errorMessage = new ErrorMessage(409,"포인트가 부족 합니다.");
            throw new GoalkeeperException(errorMessage);
        }
        if(!validateCertDatesBetweenStartAndEnd(manyTimeGoal)){
            ErrorMessage errorMessage = new ErrorMessage(409,"인증날은 [시작날,마지막날] 구간에 있어야 합니다.");
            throw new GoalkeeperException(errorMessage);
        }
        if(!validateCertDates(manyTimeGoal)){
            ErrorMessage errorMessage = new ErrorMessage(409,"인증 날은 마지막 날을 포함해 최소 4일 이상이어야 합니다.");
            throw new GoalkeeperException(errorMessage);
        }
        return goalRepository.save(manyTimeGoal);
    }
    private boolean validateStartDateAndEndDate(ManyTimeGoal manyTimeGoal){
        LocalDate now = LocalDate.now();
        LocalDate start = manyTimeGoal.getStartDate();
        LocalDate end = manyTimeGoal.getEndDate();
        if(start.isBefore(now)|| end.isBefore(now) || end.isBefore(start)) return false;
        Period period = Period.between(start,end);
        return period.getYears()>=1||period.getMonths()>=1||period.getDays()>=4;
    }
    private boolean validatePoint(Goal goal){
        User user = goal.getUser();
        return user.getPoint()>goal.getPoint();
    }
    private boolean validateCertDates(ManyTimeGoal manyTimeGoal){
        List<ManyTimeGoalCertDate> certDates = manyTimeGoal.getCertDates();
        boolean validateCertDatesCount = certDates.size()>=4;
        boolean validateCertDatesContainEndDates = certDates.get(certDates.size()-1).getCertDate().isEqual(manyTimeGoal.getEndDate());
        return validateCertDatesCount && validateCertDatesContainEndDates;
    }
    private boolean validateCertDatesBetweenStartAndEnd(ManyTimeGoal manyTimeGoal){
        List<ManyTimeGoalCertDate> certDates = manyTimeGoal.getCertDates();
        boolean validateCertDatesBetweenStartAndEnd = certDates.get(0).getCertDate().isAfter(manyTimeGoal.getStartDate().minusDays(1));
        return validateCertDatesBetweenStartAndEnd && certDates.get(certDates.size()-1).getCertDate().isEqual(manyTimeGoal.getEndDate());
    }

    @Override
    public OneTimeGoal createOneTimeGoal(OneTimeGoal oneTimeGoal) {
        if(!validatePoint(oneTimeGoal)){
            ErrorMessage errorMessage = new ErrorMessage(409,"포인트가 부족 합니다.");
            throw new GoalkeeperException(errorMessage);
        }
        if(!validateEndDate(oneTimeGoal)){
            ErrorMessage errorMessage = new ErrorMessage(400,"목표 종료날짜는 오늘보다 전날로 설정할 수 없습니다.");
            throw new GoalkeeperException(errorMessage);
        }
        return goalRepository.save(oneTimeGoal);
    }
    private boolean validateEndDate(OneTimeGoal oneTimeGoal){
        LocalDate endDate = oneTimeGoal.getEndDate();
        LocalDate now = LocalDate.now();
        Period period = Period.between(now,endDate);
        return period.getYears()>=1||period.getMonths()>=1||period.getDays()>=0;
    }

    @Override
    public Goal getGoalById(long goalId) {
        return goalRepository.findById(goalId).orElseThrow(() -> {
            ErrorMessage errorMessage = new ErrorMessage(404,"없는 goalId 입니다.");
            return new GoalkeeperException(errorMessage);
        });
    }

    @Override
    public Goal getMyGoalById(long userId, long goalId) {
        Goal goal = getGoalById(goalId);
        if(goal.getUser().getId() != userId){
            ErrorMessage errorMessage = new ErrorMessage(401,"자신이 작성한 목표만 조회할 수 있습니다.");
            throw new GoalkeeperException(errorMessage);
        }
        return goal;
    }

    @Override
    public Page<GoalResponse> getGoalsByUserId(long userId, int page) {
        Page<Goal> result = goalRepository.findAllByUser_Id(userId,makePageRequest(page));
        return makeGoalResponses(result);
    }
    private Page<GoalResponse> makeGoalResponses(Page<Goal> result) {
        return result.map(goal -> {
            if(goal instanceof ManyTimeGoal){
                return ManyTimeGoalResponse.createResponseFromEntity((ManyTimeGoal) goal);
            }else{
                return OneTimeGoalResponse.createResponseFromEntity((OneTimeGoal) goal);
            }
        });
    }
    @Override
    public Page<GoalResponse> getGoalsByUserIdAndState(long userId, GoalState goalState, int page) {
        Page<Goal> result = goalRepository.findAllByUser_IdAndGoalState(userId,goalState,makePageRequest(page));
        return makeGoalResponses(result);
    }
    @Override
    public Page<GoalResponse> getGoalsByUserIdAndCategory(long userId, CategoryType categoryType, int page) {
        Page<Goal> result = goalRepository.findAllByUser_IdAndCategory_CategoryType(userId,categoryType, makePageRequest(page));
        return makeGoalResponses(result);
    }

    @Override
    public Page<GoalResponse> getGoalsByUserIdAndCategoryAndState(long userId, CategoryType categoryType, GoalState goalState, int page) {
        Page<Goal> result = goalRepository.findAllByUser_IdAndCategory_CategoryTypeAndGoalState(userId,categoryType,goalState,makePageRequest(page));
        return makeGoalResponses(result);
    }

    @Override
    public List<GoalResponse> getGoalsByUSerIdAndNeedCertNow(long userId) {
        List<Long> goalIds = goalRepository.findGoalIdsByUSerIdAndNeedCertNow(LocalDate.now(),userId);
        List<Goal> result = goalRepository.findAllById(goalIds);
        List<GoalResponse> returnValue = new ArrayList<>();
        result.forEach(goal -> {
            if(goal instanceof ManyTimeGoal){
                returnValue.add(ManyTimeGoalResponse.createResponseFromEntity((ManyTimeGoal) goal));
            }
            if(goal instanceof OneTimeGoal){
                returnValue.add(OneTimeGoalResponse.createResponseFromEntity((OneTimeGoal) goal));
            }
        });
        return returnValue;
    }

    @Override
    public void holdGoal(User user,long goalId) {
        Goal goal = goalRepository.findById(goalId).orElseThrow(() -> {
            ErrorMessage errorMessage = new ErrorMessage(404,"없는 목표입니다.");
            return new GoalkeeperException(errorMessage);
        });
        if(!user.equals(goal.getUser())){
            ErrorMessage errorMessage = new ErrorMessage(401,"자신이 작성한 목표만 검토요청할 수 있습니다.");
            throw  new GoalkeeperException(errorMessage);
        }
        if(!goal.isHoldRequestAble()){
            ErrorMessage errorMessage = new ErrorMessage(400,"검토 요청할 수 없는 목표입니다.");
            throw new GoalkeeperException(errorMessage);
        }
        goal.hold();
        goalRepository.save(goal);
    }
}

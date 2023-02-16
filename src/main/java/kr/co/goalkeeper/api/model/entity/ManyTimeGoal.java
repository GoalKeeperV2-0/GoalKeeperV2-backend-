package kr.co.goalkeeper.api.model.entity;

import kr.co.goalkeeper.api.model.request.ManyTimeGoalRequest;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Entity
@DiscriminatorValue("ManyTimeGoal")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ManyTimeGoal extends Goal {
    @Column
    @NotNull
    private int successCount;
    @Column
    @NotNull
    private int failCount;

    @OneToMany(mappedBy = "manyTimeGoal",cascade = CascadeType.ALL)
    private List<ManyTimeGoalCertDate> certDates;
    @Builder
    private ManyTimeGoal(long id, User user, String title, String content, int point, GoalState goalState, Reward reward, Category category, int successCount, List<ManyTimeGoalCertDate> certDates) {
        super(id, user, title, content, point, goalState, reward, category);
        this.successCount = successCount;
        this.certDates = certDates;
    }
    public ManyTimeGoal(ManyTimeGoalRequest request,User user){
        this.endDate = request.getEndDate();
        this.goalState = GoalState.ONGOING;
        this.category = new Category(request.getCategoryType());
        this.point = request.getPoint();
        this.content = request.getContent();
        this.title = request.getTitle();
        this.reward = request.getReward();
        this.startDate = LocalDate.now();
        this.certDates = new ArrayList<>();
        Collections.sort(request.getCertDates());
        for (LocalDate certDate: request.getCertDates()) {
            certDates.add(ManyTimeGoalCertDate.builder()
                    .manyTimeGoal(this)
                    .certDate(certDate)
                    .build());
        }
        this.user = user;
        minusUserPoint();
    }
    public void successCertification(){
        successCount++;
        int maxSuccessCount = certDates.size();
        if(successCount == maxSuccessCount){
            success100();
        }else if(successCount>Math.round(maxSuccessCount*0.8f)){
            success80();
        }else if(successCount>Math.round(maxSuccessCount*0.7f)){
            success();
        }
    }
    private void success80(){
        goalState = GoalState.SUCCESS;
        int rewardPoint = (int) Math.round(point *0.1);
        user.plusPoint(rewardPoint,category.getCategoryType());
    }
    private void success100(){
        goalState = GoalState.SUCCESS;
        int rewardPoint = (int) Math.round(point *0.3);
        user.plusPoint(rewardPoint,category.getCategoryType());
    }

    public void failCertification(){
        failCount++;
        int maxSuccessCount = certDates.size();
        if(failCount > Math.round(maxSuccessCount*0.7f)){
            hold();
        }
    }
}

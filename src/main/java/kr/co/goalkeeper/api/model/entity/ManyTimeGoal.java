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
import java.util.List;

@Entity
@DiscriminatorValue("ManyTimeGoal")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ManyTimeGoal extends Goal {
    @Column
    @NotNull
    private LocalDate startDate;
    @Column
    @NotNull
    private LocalDate endDate;
    @Column
    @NotNull
    private int successCount;

    @OneToMany(mappedBy = "manyTimeGoal",cascade = CascadeType.ALL)
    private List<ManyTimeGoalCertDate> certDates;

    @Builder
    private ManyTimeGoal(long id, User user, String title, String content, int point, GoalState goalState, Reward reward, Category category, LocalDate startDate, LocalDate endDate) {
        super(id, user, title, content, point, goalState, reward, category);
        this.startDate = startDate;
        this.endDate = endDate;
    }
    public ManyTimeGoal(ManyTimeGoalRequest request,User user){
        this.endDate = request.getEndDate();
        this.goalState = GoalState.ONGOING;
        this.category = new Category(request.getCategoryType());
        this.point = request.getPoint();
        this.content = request.getContent();
        this.title = request.getTitle();
        this.reward = request.getReward();
        this.startDate = request.getStartDate();
        this.certDates = new ArrayList<>();
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
        user.plusPoint(rewardPoint);
    }
    private void success100(){
        goalState = GoalState.SUCCESS;
        int rewardPoint = (int) Math.round(point *0.3);
        user.plusPoint(rewardPoint);
    }
}

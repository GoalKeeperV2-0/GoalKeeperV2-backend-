package kr.co.goalkeeper.api.model.domain;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import static kr.co.goalkeeper.api.model.domain.GoalState.*;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class Goal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected long id;
    @ManyToOne
    @JoinColumn(name = "user_id")
    protected User user;
    @NotNull
    protected String title;
    @NotNull
    protected String content;
    @Min(0)
    @Max(10000)
    protected int point;
    @Column
    @Enumerated(EnumType.STRING)
    @NotNull
    protected GoalState goalState;
    @Column
    @Enumerated(EnumType.STRING)
    @NotNull
    protected Reward reward;
    @OneToOne
    @JoinColumn(name = "category")
    protected Category category;

    protected Goal(long id, User user, String title, String content, int point, GoalState goalState, Reward reward, Category category) {
        this.id = id;
        this.user = user;
        this.title = title;
        this.content = content;
        this.point = point;
        this.goalState = goalState;
        this.reward = reward;
        this.category = category;
    }
    public void success(){
        goalState = SUCCESS;
        double rewardRate = reward.getRewardRate();
        int rewardPoint = (int)Math.round(rewardRate*point);
        user.plusPoint(rewardPoint);
    }
    public void hold(){
        goalState = HOLD;
    }
    public void fail(){
        goalState = FAIL;
        double penaltyRate = reward.getPenaltyRate();
        int penaltyPoint = (int)Math.round(penaltyRate*point);
        user.plusPoint(penaltyPoint);
    }
}

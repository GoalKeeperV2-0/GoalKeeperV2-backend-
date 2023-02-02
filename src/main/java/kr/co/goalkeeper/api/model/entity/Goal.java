package kr.co.goalkeeper.api.model.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public abstract class Goal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected long id;
    @ManyToOne(cascade = CascadeType.ALL)
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
    protected void minusUserPoint(){
        if(user!=null){
            user.minusPoint(point);
        }
    }
}

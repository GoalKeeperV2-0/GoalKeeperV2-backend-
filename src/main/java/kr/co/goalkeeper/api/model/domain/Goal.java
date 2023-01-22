package kr.co.goalkeeper.api.model.domain;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

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
    @PositiveOrZero
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
    }
    public void hold(){
        goalState = HOLD;
    }
    public void fail(){
        goalState = FAIL;
    }
}

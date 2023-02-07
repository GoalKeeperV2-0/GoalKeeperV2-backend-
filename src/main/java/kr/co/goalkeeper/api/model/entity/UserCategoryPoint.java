package kr.co.goalkeeper.api.model.entity;

import lombok.Getter;

import javax.persistence.*;
import javax.validation.constraints.Positive;

@Entity
@Getter
public class UserCategoryPoint {
    @EmbeddedId
    UserCategoryPointId id;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    @MapsId("userId")
    private User user;
    @ManyToOne
    @JoinColumn(name = "category")
    @MapsId("categoryType")
    private Category category;
    @Column
    private int point;
    public void addPoint(@Positive int point){
        this.point+=point;
    }
    public void minusPoint(@Positive int point){
        this.point+=point;
    }
}

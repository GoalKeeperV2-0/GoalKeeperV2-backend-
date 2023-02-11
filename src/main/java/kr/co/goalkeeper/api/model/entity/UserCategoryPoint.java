package kr.co.goalkeeper.api.model.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Positive;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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

    public UserCategoryPoint(User user, CategoryType categoryType) {
        this.user = user;
        this.category = new Category(categoryType);
        this.point = 10000000;
    }

    @Column
    private int point;
    public void addPoint(@Positive int point){
        this.point+=point;
    }
    public void minusPoint(@Positive int point){
        this.point+=point;
    }
}

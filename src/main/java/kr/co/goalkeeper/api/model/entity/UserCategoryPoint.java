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

    @Column
    private int point;

    public UserCategoryPoint(User user, Category category) {
        id = new UserCategoryPointId();
        id.setCategoryType(category.getCategoryType());
        this.user = user;
        this.category = category;
        this.point = 10000000;
    }

    public void addPoint(@Positive int point){
        this.point+=point;
    }
    public void minusPoint(@Positive int point){
        this.point+=point;
    }
}

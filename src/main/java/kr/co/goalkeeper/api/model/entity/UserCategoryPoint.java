package kr.co.goalkeeper.api.model.entity;

import lombok.Getter;

import javax.persistence.*;

@Entity
@Getter
public class UserCategoryPoint {
    @EmbeddedId
    UserCategoryPointId id;
    @ManyToOne
    @JoinColumn(name = "user_id")
    @MapsId("userId")
    private User user;
    @ManyToOne
    @JoinColumn(name = "category")
    @MapsId("categoryType")
    private Category category;
}

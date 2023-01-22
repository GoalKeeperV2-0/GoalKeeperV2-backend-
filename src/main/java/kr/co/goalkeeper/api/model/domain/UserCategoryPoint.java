package kr.co.goalkeeper.api.model.domain;

import javax.persistence.*;

@Entity
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

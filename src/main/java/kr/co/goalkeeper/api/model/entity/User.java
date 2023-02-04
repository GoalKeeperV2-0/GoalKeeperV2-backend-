package kr.co.goalkeeper.api.model.entity;

import kr.co.goalkeeper.api.exception.GoalkeeperException;
import kr.co.goalkeeper.api.model.request.AdditionalUserInfo;
import kr.co.goalkeeper.api.model.response.ErrorMessage;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.Set;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {
    @Transient
    public static final User EMPTYUSER = new User();
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @NotNull
    private String name;

    @Column(nullable = false, updatable = false)
    @NotNull
    @Email
    private String email;

    @Column
    private String picture;

    @Column
    private Integer age;

    @Column
    @Enumerated(EnumType.STRING)
    private Sex sex;
    @Column
    @NotNull
    private boolean joinComplete;

    @Column
    @ColumnDefault("0")
    @NotNull
    private Integer point = 0;

    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL)
    private Set<UserCategoryPoint> userCategoryPointSet;

    @Builder
    private User(Long id, String name, String email, String picture, Integer age, Sex sex, boolean joinComplete, Integer point) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.picture = picture;
        this.age = age;
        this.sex = sex;
        this.joinComplete = joinComplete;
        this.point = point;
    }
    public void setAdditional(AdditionalUserInfo userInfo){
        age = userInfo.getAge();
        sex = userInfo.getSex();
    }
    public void joinComplete(){
        joinComplete = true;
    }
    protected void plusPoint(@Positive int point,CategoryType categoryType){
        this.point+=point;
        UserCategoryPoint usp = findUSPFromSetByCategoryType(categoryType);
        usp.addPoint(point);
    }
    protected void minusPoint(@Positive int point){
        this.point-=point;
    }
    protected void minusCategoryPoint(@Positive int point,CategoryType categoryType){
        UserCategoryPoint usp = findUSPFromSetByCategoryType(categoryType);
        usp.minusPoint(point);
    }
    private UserCategoryPoint findUSPFromSetByCategoryType(CategoryType categoryType){
        return userCategoryPointSet.stream().filter(userCategoryPoint -> userCategoryPoint.getCategory().getCategoryType().equals(categoryType)).findFirst().orElseThrow(()->{
            ErrorMessage errorMessage = new ErrorMessage(409,"UserCategoryPoint를 찾지 못했습니다.");
            return new GoalkeeperException(errorMessage);
        });
    }
}

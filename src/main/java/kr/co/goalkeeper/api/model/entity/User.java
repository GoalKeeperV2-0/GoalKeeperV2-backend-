package kr.co.goalkeeper.api.model.entity;

import kr.co.goalkeeper.api.exception.GoalkeeperException;
import kr.co.goalkeeper.api.model.oauth.OAuthType;
import kr.co.goalkeeper.api.model.request.AdditionalUserInfo;
import kr.co.goalkeeper.api.model.request.UpdateUserRequest;
import kr.co.goalkeeper.api.model.response.ErrorMessage;
import kr.co.goalkeeper.api.util.ImageSaver;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {
    @Transient
    public static final User EMPTYUSER = new User();
    @Transient
    private final int INIT_POINT = 10000000;
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

    public User(Map<String,String> credential, OAuthType oAuthType, List<Category> categoryList){
        switch (oAuthType){
            case GOOGLE:
                email = credential.get("email");
                name = credential.get("name");
                picture = credential.get("picture");
                point = INIT_POINT;
                joinComplete = false;
                userCategoryPointSet = new HashSet<>();
                for (Category category: categoryList) {
                    UserCategoryPoint usp = new UserCategoryPoint(this,category);
                    userCategoryPointSet.add(usp);
                }
                break;
            case NAVER:
            case KAKAO:
                ErrorMessage errorMessage = new ErrorMessage(500, "?????? ???????????? ???????????????.");
                throw new GoalkeeperException(errorMessage);
            default:
                errorMessage = new ErrorMessage(400, "???????????? ?????? ???????????????.");
                throw new GoalkeeperException(errorMessage);
        }
    }
    public User(long id){
        this.id = id;
    }
    public void setAdditional(AdditionalUserInfo userInfo){
        age = userInfo.getAge();
        sex = userInfo.getSex();
        name = userInfo.getNickName();
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
            ErrorMessage errorMessage = new ErrorMessage(409,"UserCategoryPoint??? ?????? ???????????????.");
            return new GoalkeeperException(errorMessage);
        });
    }
    public void giveVerificationReward(){
        point+=100;
    }

    public void updateUser(UpdateUserRequest updateUserRequest,String profileImageRootPath){
        if(updateUserRequest.getName()!=null){
            name = updateUserRequest.getName();
        }
        if(updateUserRequest.getPicture()!=null){
            picture = ImageSaver.saveProfileImage(profileImageRootPath,updateUserRequest.getPicture(),id);
        }
        if(updateUserRequest.getSex()!=null){
            sex = updateUserRequest.getSex();
        }
        if(updateUserRequest.getAge()!=null && updateUserRequest.getAge()>0){
            age = updateUserRequest.getAge();
        }
    }
}

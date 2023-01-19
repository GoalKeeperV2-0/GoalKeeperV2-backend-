package kr.co.goalkeeper.api.model.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.jpa.repository.Query;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class User {
    public static final User EMPTYUSER = new User();
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @NotNull
    private String name;

    @Column(nullable = false, updatable = false)
    @NotNull
    private String email;

    @Column
    private String picture;
    @Column
    private String description;
    @Column
    private Integer age;
    @Column
    @Enumerated(EnumType.STRING)
    private Sex sex;
    @Column
    @NotNull
    private boolean joinComplete;
    public void setAdditional(AdditionalUserInfo userInfo){
        description = userInfo.getDescription();
        age = userInfo.getAge();
        sex = userInfo.getSex();
    }
}

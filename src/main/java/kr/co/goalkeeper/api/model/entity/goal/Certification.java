package kr.co.goalkeeper.api.model.entity.goal;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

import static kr.co.goalkeeper.api.model.entity.goal.CertificationState.*;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn
@Getter
@NoArgsConstructor
public abstract class Certification {
    protected Certification(String content, String picture, MultipartFile pictureFile, CertificationState state, LocalDate date, int successCount, Goal goal, int failCount) {
        this.content = content;
        this.picture = picture;
        this.pictureFile = pictureFile;
        this.state = state;
        this.date = date;
        this.successCount = successCount;
        this.goal = goal;
        goal.certificationList.add(this);
        this.failCount = failCount;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected long id;
    @NotNull
    protected String content;
    @NotNull
    @Setter
    protected String picture;
    @Transient
    protected MultipartFile pictureFile;
    @Enumerated(EnumType.STRING)
    @NotNull
    protected CertificationState state = ONGOING;
    @Column
    protected LocalDate date;
    @Column
    protected int successCount;

    @ManyToOne(cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @JoinColumn(name = "goal_id",nullable = false)
    @NotNull
    protected Goal goal;
    @Column
    protected int failCount;
    public abstract void verificationSuccess();
    public abstract void verificationFail();
    protected void success(){
        state = SUCCESS;
    }
    protected void fail(){
        state = FAIL;
    }
    public abstract void changeGoalStateToWait();
}

package kr.co.goalkeeper.api.model.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

import static kr.co.goalkeeper.api.model.entity.CertificationState.*;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn
@Getter
public abstract class Certification {
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

    @ManyToOne(cascade = CascadeType.ALL)
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

}

package kr.co.goalkeeper.api.model.entity;

import lombok.Getter;

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
    protected String picture;
    @Enumerated(EnumType.STRING)
    @NotNull
    protected CertificationState state = ONGOING;
    @Column
    protected LocalDate date;

    protected void success(){
        state = SUCCESS;
    }
    protected void fail(){
        state = FAIL;
    }
}

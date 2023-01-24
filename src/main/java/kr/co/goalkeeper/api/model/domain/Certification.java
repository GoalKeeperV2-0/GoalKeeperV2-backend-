package kr.co.goalkeeper.api.model.domain;

import org.springframework.boot.context.properties.bind.DefaultValue;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import static kr.co.goalkeeper.api.model.domain.CertificationState.*;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn
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

    protected void success(){
        state = SUCCESS;
    }
    protected void fail(){
        state = FAIL;
    }
}

package kr.co.goalkeeper.api.model.response;

import kr.co.goalkeeper.api.model.entity.CertificationState;
import lombok.Getter;

import java.time.LocalDate;

import static kr.co.goalkeeper.api.model.entity.CertificationState.*;

@Getter
public abstract class CertificationResponse {
    protected long id;
    protected String content;
    protected String picture;
    protected CertificationState state;
    protected LocalDate date;
}

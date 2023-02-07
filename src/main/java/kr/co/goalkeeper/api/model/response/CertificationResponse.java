package kr.co.goalkeeper.api.model.response;

import kr.co.goalkeeper.api.model.entity.CertificationState;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public abstract class CertificationResponse {
    protected long id;
    protected String content;
    protected String picture;
    protected CertificationState state;
    protected LocalDate date;
}

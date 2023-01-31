package kr.co.goalkeeper.api.model.request;

import kr.co.goalkeeper.api.model.entity.CertificationState;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public abstract class CertificationRequest {
    protected String content;
    protected String picture;
    protected CertificationState state;
    protected LocalDate date;
}

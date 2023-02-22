package kr.co.goalkeeper.api.model.request;

import lombok.Getter;

@Getter
public class VerificationRequest {
    private long certificationId;
    private long userId;
    private boolean state;
}

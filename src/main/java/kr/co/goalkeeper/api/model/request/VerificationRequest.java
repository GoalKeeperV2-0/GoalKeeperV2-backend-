package kr.co.goalkeeper.api.model.request;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class VerificationRequest {
    public static VerificationRequest getTestInstance(long certificationId, boolean state){
        return new VerificationRequest(certificationId,state);
    }
    private long certificationId;
    private boolean state;
}

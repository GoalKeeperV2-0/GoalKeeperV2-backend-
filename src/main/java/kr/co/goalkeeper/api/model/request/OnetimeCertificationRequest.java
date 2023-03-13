package kr.co.goalkeeper.api.model.request;


import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;


@Getter
@NoArgsConstructor
public class OnetimeCertificationRequest extends CertificationRequest {
    public static OnetimeCertificationRequest getTestInstance(String content, MultipartFile picture, long goalId) {
        return new OnetimeCertificationRequest(content, picture, goalId);
    }
    private OnetimeCertificationRequest(String content, MultipartFile picture, long goalId) {
        super(content, picture, goalId);
    }
}

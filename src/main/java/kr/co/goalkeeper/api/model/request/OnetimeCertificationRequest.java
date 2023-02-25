package kr.co.goalkeeper.api.model.request;


import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;


@Getter
@NoArgsConstructor
public class OnetimeCertificationRequest extends CertificationRequest {
    public OnetimeCertificationRequest(String content, MultipartFile picture, long goalId) {
        super(content, picture, goalId);
    }
}

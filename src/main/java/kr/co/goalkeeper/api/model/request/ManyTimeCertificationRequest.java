package kr.co.goalkeeper.api.model.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;


@Getter
@NoArgsConstructor
public class ManyTimeCertificationRequest extends CertificationRequest {

    public ManyTimeCertificationRequest(String content, MultipartFile picture, long goalId) {
        super(content, picture, goalId);
    }
}

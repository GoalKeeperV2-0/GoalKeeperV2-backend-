package kr.co.goalkeeper.api.model.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;


@Getter
@NoArgsConstructor
public class ManyTimeCertificationRequest extends CertificationRequest {

    public static ManyTimeCertificationRequest getTestInstance(String content, MultipartFile picture, long goalId){
        return new ManyTimeCertificationRequest(content,picture,goalId);
    }
    private ManyTimeCertificationRequest(String content, MultipartFile picture, long goalId) {
        super(content, picture, goalId);
    }
}

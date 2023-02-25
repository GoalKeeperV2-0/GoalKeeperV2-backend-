package kr.co.goalkeeper.api.model.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@NoArgsConstructor
public abstract class CertificationRequest {
    protected String content;
    protected MultipartFile picture;
    protected long goalId;
    public void fixGoalId(long goalId){
        if(this.goalId!=goalId){
            this.goalId = goalId;
        }
    }
    public CertificationRequest(String content, MultipartFile picture, long goalId) {
        this.content = content;
        this.picture = picture;
        this.goalId = goalId;
    }
}

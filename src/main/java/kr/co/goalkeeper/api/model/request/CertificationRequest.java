package kr.co.goalkeeper.api.model.request;

import lombok.Getter;

@Getter
public abstract class CertificationRequest {
    protected String content;
    protected String picture;
    protected long goalId;
    public void fixGoalId(long goalId){
        if(this.goalId!=goalId){
            this.goalId = goalId;
        }
    }
}

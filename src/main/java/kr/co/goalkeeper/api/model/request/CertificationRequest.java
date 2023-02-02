package kr.co.goalkeeper.api.model.request;

import lombok.Getter;

import java.time.LocalDate;

@Getter
public abstract class CertificationRequest {
    protected String content;
    protected String picture;
    protected LocalDate date;
    protected long goalId;
    public void fixGoalId(long goalId){
        if(this.goalId!=goalId){
            this.goalId = goalId;
        }
    }
}

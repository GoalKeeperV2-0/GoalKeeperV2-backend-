package kr.co.goalkeeper.api.model.entity.goal;

public enum CertificationState {
    ONGOING,SUCCESS,FAIL;
    public boolean toBoolean(){
        return this.equals(SUCCESS);
    }
}

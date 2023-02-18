package kr.co.goalkeeper.api.model.entity;

public enum CertificationState {
    ONGOING,SUCCESS,FAIL;
    public boolean toBoolean(){
        return this.equals(SUCCESS);
    }
}

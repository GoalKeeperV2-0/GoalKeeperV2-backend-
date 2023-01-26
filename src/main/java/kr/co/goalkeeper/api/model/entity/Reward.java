package kr.co.goalkeeper.api.model.entity;

public enum Reward {
    HIGH_RETURN,LOW_RETURN;

    public double getRewardRate(){
        if(this == HIGH_RETURN){
            return 1.5;
        }else {
            return 1.1;
        }
    }
    public double getPenaltyRate(){
        if(this == HIGH_RETURN){
            return 0;
        }else {
            return 0.5;
        }
    }
}

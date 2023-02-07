package kr.co.goalkeeper.api.model.entity;

public enum Reward {
    HIGH_RETURN,LOW_RETURN;

    public float getRewardRate(){
        if(this == HIGH_RETURN){
            return 1.5f;
        }else {
            return 1.1f;
        }
    }
    public float getPenaltyRate(){
        if(this == HIGH_RETURN){
            return 0;
        }else {
            return 0.5f;
        }
    }
}

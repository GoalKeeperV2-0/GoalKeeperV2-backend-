package kr.co.goalkeeper.api.scheduler;

/**
 * 상태를 바꾸고 상태변경 메세지를 생성하는 스케줄러
 */
public interface ChangeGoalScheduler {
    void checkTimeOutGoal();
    void checkTimeOutGoalButCertRemain();
    void checkNoCertificationAtCertDay();
}

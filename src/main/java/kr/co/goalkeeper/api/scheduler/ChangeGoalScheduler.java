package kr.co.goalkeeper.api.scheduler;

/**
 * 상태를 바꾸고 상태변경 메세지를 생성하는 스케줄러
 */
public interface ChangeGoalScheduler {
    /**
     * 마감 기한이 지난 목표 처리
     */
    void checkTimeOutGoal();

    /**
     * 마감기한이 지났지만, 마지막 인증이 올라오지 않아 정산중이 아닌 지속목표 처리
     */
    void checkTimeOutGoalButCertRemain();

    /**
     * 인증날에 인증이 올라오지 않은 지속 목표 처리
     */
    void checkNoCertificationAtCertDay();
}

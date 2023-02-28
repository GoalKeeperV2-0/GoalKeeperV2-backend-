package kr.co.goalkeeper.api.scheduler;

/**
 * 상태를 바꾸지 않고 오직 메세지만 생성하는 스케줄러
 */
public interface OnlyMessagingScheduler {
    void sendCertDDayMessage();
    void sendGoalDDayMessage();
}

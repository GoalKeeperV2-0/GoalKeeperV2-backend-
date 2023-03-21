package kr.co.goalkeeper.api;

import kr.co.goalkeeper.api.scheduler.CronChangeGoalSchedulerV2;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class GoalkeeperApplication {

	public static void main(String[] args) {
		ApplicationContext applicationContext = SpringApplication.run(GoalkeeperApplication.class, args);
		NotificationSender.init(applicationContext);
		CronChangeGoalSchedulerV2 cronChangeGoalSchedulerV2 = applicationContext.getBean(CronChangeGoalSchedulerV2.class);
		cronChangeGoalSchedulerV2.checkTimeOutGoal();
		cronChangeGoalSchedulerV2.checkNoCertificationAtCertDay();
	}

}

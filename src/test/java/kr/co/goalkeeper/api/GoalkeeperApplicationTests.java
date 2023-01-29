package kr.co.goalkeeper.api;

import kr.co.goalkeeper.api.repository.ManyTimeCertificationRepository;
import kr.co.goalkeeper.api.repository.ManyTimeGoalRepository;
import kr.co.goalkeeper.api.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.List;

@SpringBootTest
class GoalkeeperApplicationTests {
	@Autowired
	ManyTimeCertificationRepository manyTimeCertificationRepository;
	@Autowired
	ManyTimeGoalRepository manyTimeGoalRepository;
	@Autowired
	private UserRepository userRepository;

	@Test
	void contextLoads() {
	}

}

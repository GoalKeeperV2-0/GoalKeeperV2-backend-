package kr.co.goalkeeper.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import kr.co.goalkeeper.api.model.entity.*;
import kr.co.goalkeeper.api.model.request.ManyTimeGoalRequest;
import kr.co.goalkeeper.api.model.response.ManyTimeGoalResponse;
import kr.co.goalkeeper.api.repository.*;
import kr.co.goalkeeper.api.service.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

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
	@Autowired
	private OneTimeCertificationService oneTimeCertificationService;
	@Autowired
	private VerificationRepository verificationRepository;
	@Autowired
	OneTimeCertificationRepository oneTimeCertificationRepository;
	@Autowired
	GoalKeeperTokenService goalKeeperTokenService;
	@Autowired
	UserService userService;
	@Autowired
	ManyTimeGoalService manyTimeGoalService;

	@Test
	@Transactional
	void contextLoads() {
		OneTimeCertification certification = oneTimeCertificationService.getCertificationByGoalId(3);
		User user = userRepository.findById(2L).get();
		Verification verification = new Verification(certification,user,true);
		verificationRepository.save(verification);
	}
	@Test
	@Transactional
	void test() throws JsonProcessingException {
		long userId = goalKeeperTokenService.getUserId("eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhY2Nlc3MiLCJ1c2VySUQiOjEsImV4cCI6MTY5MDUzMzA4OCwiaWF0IjoxNjc0OTgxMDg4LCJpc3MiOiJnb2FsS2VlcGVyMiJ9.hLshyCtb0FJRkdwwwYP6zJ_IEtKORinjZQTSnrJJAe4");
		User user = userService.getUserById(userId);
		ManyTimeGoalRequest manyTimeGoalRequest = new ObjectMapper().registerModule(new JavaTimeModule()).readValue("{\n" +
				"  \"startDate\": \"2023-02-08\",\n" +
				"  \"endDate\": \"2023-02-13\",\n" +
				"  \"certDates\": [\n" +
				"    \"2023-02-08\",\"2023-02-11\",\"2023-02-12\",\"2023-02-13\"\n" +
				"  ],\n" +
				"  \"title\": \"dd\",\n" +
				"  \"categoryType\": \"STUDY\",\n" +
				"  \"content\": \"dd\",\n" +
				"  \"point\": 100,\n" +
				"  \"reward\": \"HIGH_RETURN\"\n" +
				"}",ManyTimeGoalRequest.class);
		ManyTimeGoal manyTimeGoal = new ManyTimeGoal(manyTimeGoalRequest,user);
		manyTimeGoal = manyTimeGoalService.createManyTimeGoal(manyTimeGoal);
		ManyTimeGoalResponse result = new ManyTimeGoalResponse(manyTimeGoal);
	}

}

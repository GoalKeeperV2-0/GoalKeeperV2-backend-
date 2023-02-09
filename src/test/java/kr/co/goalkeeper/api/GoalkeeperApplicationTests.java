package kr.co.goalkeeper.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import kr.co.goalkeeper.api.exception.GoalkeeperException;
import kr.co.goalkeeper.api.model.entity.*;
import kr.co.goalkeeper.api.model.request.OneTimeGoalRequest;
import kr.co.goalkeeper.api.service.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;


@SpringBootTest
class GoalkeeperApplicationTests {
	@Autowired
	OneTimeGoalService oneTimeGoalService;
	@Autowired
	ManyTimeGoalService manyTimeGoalService;
	@Autowired
	OneTimeCertificationService oneTimeCertificationService;
	@Autowired
	ManyTimeCertificationService manyTimeCertificationService;
	@Autowired
	GoalKeeperTokenService goalKeeperTokenService;
	@Autowired
	UserService userService;

	@Test
	@Transactional
	void OneTimeGoalServiceTest() throws JsonProcessingException {
		final User user1 = userService.getUserById(1);
		createFailOneTimeGoalTest(user1);
		OneTimeGoal created = createSuccessOneTimeGoalTest(user1);
		getOneTimeGoalTest(created);
	}
	private void createFailOneTimeGoalTest(User owner) throws JsonProcessingException {
		String createGoalRequestString ="{\n" +
				"  \"endDate\": \"2023-02-03\",\n" +
				"  \"title\": \"test\",\n" +
				"  \"categoryType\": \"STUDY\",\n" +
				"  \"content\": \"test\",\n" +
				"  \"point\": 100,\n" +
				"  \"reward\": \"HIGH_RETURN\"\n" +
				"}";
		ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
		OneTimeGoalRequest cantCreate = objectMapper.readValue(createGoalRequestString,OneTimeGoalRequest.class);
		assertThrows(GoalkeeperException.class,()-> oneTimeGoalService.createOneTimeGoal(new OneTimeGoal(cantCreate,owner)));

		OneTimeGoalRequest cantCreate2 = objectMapper.readValue(createGoalRequestString.replace("2023-02-03",LocalDate.now().toString()),OneTimeGoalRequest.class);
		assertThrows(GoalkeeperException.class,()-> oneTimeGoalService.createOneTimeGoal(new OneTimeGoal(cantCreate2,owner)));
	}
	private OneTimeGoal createSuccessOneTimeGoalTest(User owner) throws JsonProcessingException {
		String createGoalRequestString ="{\n" +
				"  \"endDate\": \"2023-02-03\",\n" +
				"  \"title\": \"test\",\n" +
				"  \"categoryType\": \"STUDY\",\n" +
				"  \"content\": \"test\",\n" +
				"  \"point\": 100,\n" +
				"  \"reward\": \"HIGH_RETURN\"\n" +
				"}";
		ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
		createGoalRequestString = createGoalRequestString.replace("2023-02-03", LocalDate.now().plusDays(14).toString());
		OneTimeGoalRequest canCreate = objectMapper.readValue(createGoalRequestString,OneTimeGoalRequest.class);
		return oneTimeGoalService.createOneTimeGoal(new OneTimeGoal(canCreate,owner));
	}
	private void getOneTimeGoalTest(OneTimeGoal required){
		assertEquals(required,oneTimeGoalService.getOneTimeGoalById(required.getId()));
		assertEquals(required,oneTimeGoalService.getOneTimeGoalsByUserId(required.getUser().getId(),0).get().findFirst().get());
	}
}

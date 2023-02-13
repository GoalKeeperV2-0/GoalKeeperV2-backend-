package kr.co.goalkeeper.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import kr.co.goalkeeper.api.exception.GoalkeeperException;
import kr.co.goalkeeper.api.model.entity.*;
import kr.co.goalkeeper.api.model.request.OneTimeGoalRequest;
import kr.co.goalkeeper.api.model.response.CertificationResponse;
import kr.co.goalkeeper.api.model.response.ManyTimeCertificationResponse;
import kr.co.goalkeeper.api.model.response.OneTimeCertificationResponse;
import kr.co.goalkeeper.api.repository.CertificationRepository;
import kr.co.goalkeeper.api.service.port.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


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
	CredentialService credentialService;
	@Autowired
	CertificationRepository certificationRepository;
	@Autowired
	GoalGetService goalGetService;
	@Autowired
	CertificationGetService certificationGetService;

	@Test
	@Transactional
	void CredentialServiceTest(){

	}
	@Test
	@Transactional
	void OneTimeGoalServiceTest() throws JsonProcessingException {
		final User user1 = credentialService.getUserById(1);
		createFailOneTimeGoalTest();
		OneTimeGoal created = createSuccessOneTimeGoalTest();
		getOneTimeGoalTest(created);
	}
	private void createFailOneTimeGoalTest() throws JsonProcessingException {
		final User user1 = credentialService.getUserById(1);
		String createGoalRequestString ="{\n" +
				"  \"endDate\": \"2023-02-03\",\n" +
				"  \"title\": \"test\",\n" +
				"  \"categoryType\": \"STUDY\",\n" +
				"  \"content\": \"test\",\n" +
				"  \"point\": 600,\n" +
				"  \"reward\": \"HIGH_RETURN\"\n" +
				"}";
		ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
		OneTimeGoalRequest cantCreate = objectMapper.readValue(createGoalRequestString,OneTimeGoalRequest.class);
		assertThrows(GoalkeeperException.class,()-> oneTimeGoalService.createOneTimeGoal(new OneTimeGoal(cantCreate,user1)));

		OneTimeGoalRequest cantCreate2 = objectMapper.readValue(createGoalRequestString.replace("2023-02-03",LocalDate.now().toString()),OneTimeGoalRequest.class);
		assertThrows(GoalkeeperException.class,()-> oneTimeGoalService.createOneTimeGoal(new OneTimeGoal(cantCreate2,user1)));

		final User user2 = credentialService.getUserById(2);
		OneTimeGoalRequest cantCreate3 = objectMapper.readValue(createGoalRequestString.replace("2023-02-03",LocalDate.now().plusDays(14).toString()),OneTimeGoalRequest.class);
		assertThrows(GoalkeeperException.class,()-> oneTimeGoalService.createOneTimeGoal(new OneTimeGoal(cantCreate3,user2)));
	}
	private OneTimeGoal createSuccessOneTimeGoalTest() throws JsonProcessingException {
		final User user1 = credentialService.getUserById(1);

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
		return oneTimeGoalService.createOneTimeGoal(new OneTimeGoal(canCreate,user1));
	}
	private void getOneTimeGoalTest(OneTimeGoal required){
		assertEquals(required,goalGetService.getGoalById(required.getId()));
		assertEquals(required,goalGetService.getGoalsByUserId(required.getUser().getId(),0).get().findFirst().get());
		assertEquals(required,goalGetService.getGoalsByUserIdAndCategory(required.getUser().getId(),CategoryType.STUDY,0).get().findFirst().get());
		assertTrue(goalGetService.getGoalsByUserIdAndCategory(required.getUser().getId(), CategoryType.ETC, 0).get().findAny().isEmpty());
	}

	@Test
	@Transactional
	void OneTimeCertificationServiceTest(){

	}

	@Test
	@Transactional
	void ManyTimeGoalServiceTest(){

	}

	@Test
	@Transactional
	void ManyTimeCertificationServiceTest(){

	}
	@Test
	@Transactional
	void CertificationRepoTest() throws JsonProcessingException {
		List<Certification> certificationList = certificationRepository.findAllByGoal_Category_CategoryType(CategoryType.STUDY);
		List<CertificationResponse> responses = new ArrayList<>();
		for (Certification certification: certificationList) {
			if(certification instanceof ManyTimeCertification){
				responses.add(new ManyTimeCertificationResponse((ManyTimeCertification) certification));
			}else {
				responses.add(new OneTimeCertificationResponse((OneTimeCertification) certification));
			}
		}
		ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
		System.out.println(objectMapper.writeValueAsString(responses));
	}
}

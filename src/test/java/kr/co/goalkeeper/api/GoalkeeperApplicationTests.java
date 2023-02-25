package kr.co.goalkeeper.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import kr.co.goalkeeper.api.model.entity.*;
import kr.co.goalkeeper.api.model.request.*;
import kr.co.goalkeeper.api.model.response.GoalKeeperToken;
import kr.co.goalkeeper.api.service.port.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
class GoalkeeperApplicationTests {
	@Autowired
	CredentialService credentialService;

	@Autowired
	CertificationGetService certificationGetService;
	@Autowired
	ManyTimeCertificationService manyTimeCertificationService;
	@Autowired
	OneTimeCertificationService oneTimeCertificationService;
	@Autowired
	GoalGetService goalGetService;
	@Autowired
	OneTimeGoalService oneTimeGoalService;
	@Autowired
	ManyTimeGoalService manyTimeGoalService;
	@Autowired
	VerificationService verificationService;

	ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
	String user3JoinRequest = "{\n" +
			"  \"email\": \"test@test.com\",\n" +
			"  \"name\": \"tester\",\n" +
			"  \"password\": \"test1234!\",\n" +
			"  \"sex\": \"MAN\",\n" +
			"  \"age\": 20,\n" +
			"  \"picture\": \"null\"\n" +
			"}";
	String user3LoginString = "{\n" +
			"  \"email\": \"test@test.com\",\n" +
			"  \"password\": \"test1234!\"\n" +
			"}";
	String adduserInfo = "{\n" +
			"  \"nickName\": \"robin\",\n" +
			"  \"age\": 26,\n" +
			"  \"sex\": \"MAN\"\n" +
			"}";

	/**
	 * 회원가입 로그인 테스트
	 * @throws JsonProcessingException
	 */
	@Test
	@Transactional
	void join_loginTest() throws JsonProcessingException {
		JoinRequest user3joinRequest = objectMapper.readValue(user3JoinRequest,JoinRequest.class);
		GoalKeeperToken goalKeeperToken3 = credentialService.join(user3joinRequest);
		long user3Id = credentialService.getUserId(goalKeeperToken3.getAccessToken());
		User user3 = credentialService.getUserById(user3Id);
		LoginRequest user3LoginRequest = objectMapper.readValue(user3LoginString, LoginRequest.class);
		GoalKeeperToken goalKeeperToken3_1 = credentialService.loginByEmailPassword(user3LoginRequest);
		User user3_login = credentialService.getUserById(credentialService.getUserId(goalKeeperToken3_1.getAccessToken()));
		assertThat(user3_login).isEqualTo(user3);
	}

	/**
	 * sns 회원가입 후 추가정보 입력 테스트
	 * @throws JsonProcessingException
	 */
	@Test
	@Transactional
	void joinCompleteTest() throws JsonProcessingException {
		AdditionalUserInfo additionalUserInfo = objectMapper.readValue(adduserInfo, AdditionalUserInfo.class);
		credentialService.joinCompleteAfterOAuthJoin(2,additionalUserInfo);
		User user2 = credentialService.getUserById(2);
		assertThat(user2.getAge()).isEqualTo(26);
	}

	/**
	 * 목표 조회 테스트
	 */
	@Test
	@Transactional
	void goalGetTest(){
		Page<Goal> goals = goalGetService.getGoalsByUserId(1,0);
		List<Goal> goalList = goals.getContent();
		assertThat(goalList.size()).isEqualTo(2);
		assertThat(goalList.get(0).getClass()).isEqualTo(OneTimeGoal.class);
		assertThat(goalList.get(1).getClass()).isEqualTo(ManyTimeGoal.class);
	}

	/**
	 * 목표 등록 테스트
	 */
	@Test
	@Transactional
	void goalAddTest() throws JsonProcessingException {
		String goalRequestString = "{\n" +
				"  \"title\": \"test_900fb545d661\",\n" +
				"  \"categoryType\": \"ETC\",\n" +
				"  \"content\": \"test_1f226eb6762c\",\n" +
				"  \"point\": 200,\n" +
				"  \"reward\": \"HIGH_RETURN\",\n" +
				"  \"endDate\": \"2029-04-26\"\n" +
				"}";
		OneTimeGoalRequest goalRequest = objectMapper.readValue(goalRequestString,OneTimeGoalRequest.class);
		User user = credentialService.getUserById(1);
		OneTimeGoal oneTimeGoal = new OneTimeGoal(goalRequest,user);
		OneTimeGoal added = oneTimeGoalService.createOneTimeGoal(oneTimeGoal);
		assertThat(added).isEqualTo(oneTimeGoal);
		Page<Goal> goalPage = goalGetService.getGoalsByUserId(1,0);
		assertThat(goalPage.stream().anyMatch(goal -> goal.equals(added))).isEqualTo(true);

		//지속 목표 등록
		String manyTimeGoalString = "{\n" +
				"  \"endDate\": \"End\",\n" +
				"  \"certDates\": [\n" +
				"    \"one\",\"two\",\"three\",\"four\",\"five\",\"six\"\n" +
				"  ],\n" +
				"  \"title\": \"목표제목\",\n" +
				"  \"categoryType\": \"STUDY\",\n" +
				"  \"content\": \"목표본문\",\n" +
				"  \"point\": 200,\n" +
				"  \"reward\": \"HIGH_RETURN\"\n" +
				"}";
		manyTimeGoalString = manyTimeGoalString.replace("End", LocalDate.now().plusDays(5).toString())
				.replace("one",LocalDate.now().toString())
				.replace("two",LocalDate.now().plusDays(1).toString())
				.replace("three",LocalDate.now().plusDays(2).toString())
				.replace("four",LocalDate.now().plusDays(3).toString())
				.replace("five",LocalDate.now().plusDays(4).toString())
				.replace("six",LocalDate.now().plusDays(5).toString());
		ManyTimeGoalRequest manyTimeGoalRequest = objectMapper.readValue(manyTimeGoalString,ManyTimeGoalRequest.class);
		User user1 = credentialService.getUserById(1);
		ManyTimeGoal manyTimeGoal = new ManyTimeGoal(manyTimeGoalRequest,user1);
		ManyTimeGoal addedMany = manyTimeGoalService.createManyTimeGoal(manyTimeGoal);
		assertThat(addedMany).isEqualTo(manyTimeGoal);
		goalPage = goalGetService.getGoalsByUserId(1,0);
		assertThat(goalPage.stream().anyMatch(goal -> goal.equals(addedMany))).isEqualTo(true);

		// 지속 목표 등록 실패 - 목표 시작날짜는 목표 종료날짜보다 4일 이상 빨라야 합니다.
		addManyGoalFail1();
		addManyGoalFail1_1();
		// 지속 목표 등록 실패 - 포인트 부족.
		addManyGoalFail2();
		// 지속 목표 등록 실패 - 인증날은 [시작날,마지막날] 구간에 있어야 합니다.
		addManyGoalFail3();
		// 지속 목표 등록 실패 - 인증 날은 마지막 날을 포함해 최소 4일 이상이어야 합니다.
		addManyGoalFail4();
	}
	private void addManyGoalFail1() throws JsonProcessingException {
		String manyTimeGoalString = "{\n" +
				"  \"endDate\": \"End\",\n" +
				"  \"certDates\": [\n" +
				"    \"one\",\"two\",\"three\"\n" +
				"  ],\n" +
				"  \"title\": \"목표제목\",\n" +
				"  \"categoryType\": \"STUDY\",\n" +
				"  \"content\": \"목표본문\",\n" +
				"  \"point\": 200,\n" +
				"  \"reward\": \"HIGH_RETURN\"\n" +
				"}";
		manyTimeGoalString = manyTimeGoalString.replace("End", LocalDate.now().plusDays(3).toString())
				.replace("one",LocalDate.now().toString())
				.replace("two",LocalDate.now().plusDays(1).toString())
				.replace("three",LocalDate.now().plusDays(2).toString());
		ManyTimeGoalRequest manyTimeGoalRequest = objectMapper.readValue(manyTimeGoalString,ManyTimeGoalRequest.class);
		User user1 = credentialService.getUserById(1);
		ManyTimeGoal manyTimeGoal = new ManyTimeGoal(manyTimeGoalRequest,user1);
		try {
			ManyTimeGoal addedMany = manyTimeGoalService.createManyTimeGoal(manyTimeGoal);
		}catch (Exception e){
			assertThat(e).hasMessage("목표 시작날짜는 목표 종료날짜보다 4일 이상 빨라야 합니다.\n목표 시작날짜와 목표 종료날짜는 오늘보다 빠르면 안됩니다.");
		}
	}
	private void addManyGoalFail1_1() throws JsonProcessingException {
		String manyTimeGoalString = "{\n" +
				"  \"endDate\": \"End\",\n" +
				"  \"certDates\": [\n" +
				"    \"one\",\"two\",\"three\"\n" +
				"  ],\n" +
				"  \"title\": \"목표제목\",\n" +
				"  \"categoryType\": \"STUDY\",\n" +
				"  \"content\": \"목표본문\",\n" +
				"  \"point\": 200,\n" +
				"  \"reward\": \"HIGH_RETURN\"\n" +
				"}";
		manyTimeGoalString = manyTimeGoalString.replace("End", LocalDate.now().minusDays(3).toString())
				.replace("one",LocalDate.now().toString())
				.replace("two",LocalDate.now().plusDays(1).toString())
				.replace("three",LocalDate.now().plusDays(2).toString());
		ManyTimeGoalRequest manyTimeGoalRequest = objectMapper.readValue(manyTimeGoalString,ManyTimeGoalRequest.class);
		User user1 = credentialService.getUserById(1);
		ManyTimeGoal manyTimeGoal = new ManyTimeGoal(manyTimeGoalRequest,user1);
		try {
			ManyTimeGoal addedMany = manyTimeGoalService.createManyTimeGoal(manyTimeGoal);
		}catch (Exception e){
			assertThat(e).hasMessage("목표 시작날짜는 목표 종료날짜보다 4일 이상 빨라야 합니다.\n목표 시작날짜와 목표 종료날짜는 오늘보다 빠르면 안됩니다.");
		}
	}
	private void addManyGoalFail2() throws JsonProcessingException {
		String manyTimeGoalString = "{\n" +
				"  \"endDate\": \"End\",\n" +
				"  \"certDates\": [\n" +
				"    \"one\",\"two\",\"three\",\"four\",\"five\",\"six\"\n" +
				"  ],\n" +
				"  \"title\": \"목표제목\",\n" +
				"  \"categoryType\": \"STUDY\",\n" +
				"  \"content\": \"목표본문\",\n" +
				"  \"point\": 10000,\n" +
				"  \"reward\": \"HIGH_RETURN\"\n" +
				"}";
		manyTimeGoalString = manyTimeGoalString.replace("End", LocalDate.now().plusDays(5).toString())
				.replace("one",LocalDate.now().toString())
				.replace("two",LocalDate.now().plusDays(1).toString())
				.replace("three",LocalDate.now().plusDays(2).toString())
				.replace("four",LocalDate.now().plusDays(3).toString())
				.replace("five",LocalDate.now().plusDays(4).toString())
				.replace("six",LocalDate.now().plusDays(5).toString());
		ManyTimeGoalRequest manyTimeGoalRequest = objectMapper.readValue(manyTimeGoalString,ManyTimeGoalRequest.class);
		User user2 = credentialService.getUserById(2);
		ManyTimeGoal manyTimeGoal = new ManyTimeGoal(manyTimeGoalRequest,user2);
		try {
			ManyTimeGoal addedMany = manyTimeGoalService.createManyTimeGoal(manyTimeGoal);
		}catch (Exception e){
			assertThat(e).hasMessage("포인트가 부족 합니다.");
		}
	}
	private void addManyGoalFail3() throws JsonProcessingException {
		String manyTimeGoalString = "{\n" +
				"  \"endDate\": \"End\",\n" +
				"  \"certDates\": [\n" +
				"    \"one\",\"two\",\"three\",\"four\",\"five\",\"six\"\n" +
				"  ],\n" +
				"  \"title\": \"목표제목\",\n" +
				"  \"categoryType\": \"STUDY\",\n" +
				"  \"content\": \"목표본문\",\n" +
				"  \"point\": 200,\n" +
				"  \"reward\": \"HIGH_RETURN\"\n" +
				"}";
		manyTimeGoalString = manyTimeGoalString.replace("End", LocalDate.now().plusDays(5).toString())
				.replace("one",LocalDate.now().toString())
				.replace("two",LocalDate.now().plusDays(1).toString())
				.replace("three",LocalDate.now().plusDays(2).toString())
				.replace("four",LocalDate.now().plusDays(3).toString())
				.replace("five",LocalDate.now().plusDays(4).toString())
				.replace("six",LocalDate.now().plusDays(6).toString());
		ManyTimeGoalRequest manyTimeGoalRequest = objectMapper.readValue(manyTimeGoalString,ManyTimeGoalRequest.class);
		User user1 = credentialService.getUserById(1);
		ManyTimeGoal manyTimeGoal = new ManyTimeGoal(manyTimeGoalRequest,user1);
		try {
			ManyTimeGoal addedMany = manyTimeGoalService.createManyTimeGoal(manyTimeGoal);
		}catch (Exception e){
			assertThat(e).hasMessage("인증날은 [시작날,마지막날] 구간에 있어야 합니다.");
		}
	}
	private void addManyGoalFail4() throws JsonProcessingException {
		String manyTimeGoalString = "{\n" +
				"  \"endDate\": \"End\",\n" +
				"  \"certDates\": [\n" +
				"    \"one\",\"two\",\"three\"\n" +
				"  ],\n" +
				"  \"title\": \"목표제목\",\n" +
				"  \"categoryType\": \"STUDY\",\n" +
				"  \"content\": \"목표본문\",\n" +
				"  \"point\": 200,\n" +
				"  \"reward\": \"HIGH_RETURN\"\n" +
				"}";
		manyTimeGoalString = manyTimeGoalString.replace("End", LocalDate.now().plusDays(5).toString())
				.replace("one",LocalDate.now().toString())
				.replace("two",LocalDate.now().plusDays(1).toString())
				.replace("three",LocalDate.now().plusDays(5).toString());
		ManyTimeGoalRequest manyTimeGoalRequest = objectMapper.readValue(manyTimeGoalString,ManyTimeGoalRequest.class);
		User user1 = credentialService.getUserById(1);
		ManyTimeGoal manyTimeGoal = new ManyTimeGoal(manyTimeGoalRequest,user1);
		try {
			ManyTimeGoal addedMany = manyTimeGoalService.createManyTimeGoal(manyTimeGoal);
		}catch (Exception e){
			assertThat(e).hasMessage("인증 날은 마지막 날을 포함해 최소 4일 이상이어야 합니다.");
		}
	}


	/**
	 * 인증 조회 테스트
	 */
	@Test
	@Transactional
	void certGetTest(){
		Page<Certification> page = certificationGetService.getCertifications(0);
		assertThat(page.getTotalElements()).isEqualTo(2);
		assertThat(page.getContent().get(0).getClass()).isEqualTo(OneTimeCertification.class);
		assertThat(page.getContent().get(1).getClass()).isEqualTo(ManyTimeCertification.class);
	}

	/**
	 * 인증 등록 테스트
	 */
	@Test
	@Transactional
	void certAddTest() throws JsonProcessingException {
		// 지속목표 등록
		String manyTimeGoalString = "{\n" +
				"  \"endDate\": \"End\",\n" +
				"  \"certDates\": [\n" +
				"    \"one\",\"two\",\"three\",\"four\",\"five\",\"six\"\n" +
				"  ],\n" +
				"  \"title\": \"목표제목\",\n" +
				"  \"categoryType\": \"STUDY\",\n" +
				"  \"content\": \"목표본문\",\n" +
				"  \"point\": 200,\n" +
				"  \"reward\": \"HIGH_RETURN\"\n" +
				"}";
		manyTimeGoalString = manyTimeGoalString.replace("End", LocalDate.now().plusDays(5).toString())
				.replace("one",LocalDate.now().toString())
				.replace("two",LocalDate.now().plusDays(1).toString())
				.replace("three",LocalDate.now().plusDays(2).toString())
				.replace("four",LocalDate.now().plusDays(3).toString())
				.replace("five",LocalDate.now().plusDays(4).toString())
				.replace("six",LocalDate.now().plusDays(5).toString());
		ManyTimeGoalRequest goalRequest = objectMapper.readValue(manyTimeGoalString,ManyTimeGoalRequest.class);
		User user1 = credentialService.getUserById(1);
		ManyTimeGoal manyTimeGoal = new ManyTimeGoal(goalRequest,user1);
		manyTimeGoal = manyTimeGoalService.createManyTimeGoal(manyTimeGoal);
		//인증 등록
		String certAddRequestString = "{\n" +
				"  \"content\": \"test_2ddc8dc64f59\",\n" +
				"  \"picture\": \"test_0bd2f129d410\",\n" +
				"  \"goalId\": goalID\n" +
				"}";
		certAddRequestString = certAddRequestString.replace("goalID",manyTimeGoal.getId()+"");
		ManyTimeCertificationRequest certificationRequest = new ManyTimeCertificationRequest("ddd",new MockMultiPartFile(),manyTimeGoal.getId());
		ManyTimeCertification certification = new ManyTimeCertification(certificationRequest,manyTimeGoal);
		manyTimeCertificationService.createCertification(certification,1);

		// 예외 발생할 요청 - 이미 등록된 날짜에 인증 등록
		try {
			manyTimeCertificationService.createCertification(certification, 1);
		}catch (Exception e){
			assertThat(e).hasMessage("이미 인증이 등록된 인증날 입니다.");
		}

		// 예외 발생할 요청 - 다른 사람이 등록
		try {
			manyTimeCertificationService.createCertification(certification, 2);
		}catch (Exception e){
			assertThat(e).hasMessage("자신이 작성한 목표의 인증만 등록할 수 있습니다.");
		}
		Page<Certification> page = certificationGetService.getCertifications(0);
		assertThat(page.getTotalElements()).isEqualTo(3);
		assertThat(page.getContent().get(2).getGoal().getId()).isEqualTo(certificationRequest.getGoalId());

		// 일반 목표 등록
		String onetimegoalString = "{\n" +
				"  \"title\": \"test_055863b0e77b\",\n" +
				"  \"categoryType\": \"STUDY\",\n" +
				"  \"content\": \"test_b54748291e78\",\n" +
				"  \"point\": 2000,\n" +
				"  \"reward\": \"HIGH_RETURN\",\n" +
				"  \"endDate\": \"2026-08-04\"\n" +
				"}";
		OneTimeGoalRequest oneTimeGoalRequest = objectMapper.readValue(onetimegoalString,OneTimeGoalRequest.class);
		OneTimeGoal oneTimeGoal = new OneTimeGoal(oneTimeGoalRequest,user1);
		oneTimeGoal = oneTimeGoalService.createOneTimeGoal(oneTimeGoal);
		// 일반 목표 인증 등록
		String onetimecertString = "{\n" +
				"  \"content\": \"test_33056dd5a082\",\n" +
				"  \"picture\": \"test_c2c175d7c346\",\n" +
				"  \"goalId\": goalID\n" +
				"}";
		onetimecertString = onetimecertString.replace("goalID",oneTimeGoal.getId()+"");
		OnetimeCertificationRequest onetimeCertificationRequest = new OnetimeCertificationRequest("ttt",new MockMultiPartFile(),oneTimeGoal.getId());
		OneTimeCertification oneTimeCertification = new OneTimeCertification(onetimeCertificationRequest,oneTimeGoal);
		oneTimeCertificationService.createCertification(oneTimeCertification,user1.getId());
		// 예외 발생할 요청 - 이미 인증이 등록된 경우
		try{
			oneTimeCertificationService.createCertification(oneTimeCertification,user1.getId());
		}catch (Exception e){
			assertThat(e).hasMessage("이미 인증이 등록된 목표입니다.");
		}
		// Todo 예외 발생한 경우 - 진행중이 아닌 목표에 인증을 등록할 경우
	}
	class MockMultiPartFile implements MultipartFile{

		@Override
		public String getName() {
			return null;
		}

		@Override
		public String getOriginalFilename() {
			return "test.jpg";
		}

		@Override
		public String getContentType() {
			return null;
		}

		@Override
		public boolean isEmpty() {
			return false;
		}

		@Override
		public long getSize() {
			return 0;
		}

		@Override
		public byte[] getBytes() throws IOException {
			return new byte[0];
		}

		@Override
		public InputStream getInputStream() throws IOException {
			return null;
		}

		@Override
		public void transferTo(File dest) throws IOException, IllegalStateException {
		}
	}
	/**
	 * 자기 정보 조회 테스트
	 */
	@Test
	@Transactional
	void userInfoTest(){

	}

	/**
	 * 회원정보 수정 테스트
	 */
	@Test
	@Transactional
	void userUpdateTest(){

	}

	/**
	 * 다른 회원 정보 조회 테스트
	 */
	@Test
	@Transactional
	void otherUserInfoTest(){

	}
	@Test
	@Transactional
	void VerificationTest() throws JsonProcessingException {
		// 지속목표 등록
		String manyTimeGoalString = "{\n" +
				"  \"endDate\": \"End\",\n" +
				"  \"certDates\": [\n" +
				"    \"one\",\"two\",\"three\",\"four\",\"five\",\"six\"\n" +
				"  ],\n" +
				"  \"title\": \"목표제목\",\n" +
				"  \"categoryType\": \"STUDY\",\n" +
				"  \"content\": \"목표본문\",\n" +
				"  \"point\": 200,\n" +
				"  \"reward\": \"HIGH_RETURN\"\n" +
				"}";
		manyTimeGoalString = manyTimeGoalString.replace("End", LocalDate.now().plusDays(5).toString())
				.replace("one",LocalDate.now().toString())
				.replace("two",LocalDate.now().plusDays(1).toString())
				.replace("three",LocalDate.now().plusDays(2).toString())
				.replace("four",LocalDate.now().plusDays(3).toString())
				.replace("five",LocalDate.now().plusDays(4).toString())
				.replace("six",LocalDate.now().plusDays(5).toString());
		ManyTimeGoalRequest goalRequest = objectMapper.readValue(manyTimeGoalString,ManyTimeGoalRequest.class);
		User user1 = credentialService.getUserById(1);
		ManyTimeGoal manyTimeGoal = new ManyTimeGoal(goalRequest,user1);
		manyTimeGoal = manyTimeGoalService.createManyTimeGoal(manyTimeGoal);
		//인증 등록
		String certAddRequestString = "{\n" +
				"  \"content\": \"test_2ddc8dc64f59\",\n" +
				"  \"picture\": \"test_0bd2f129d410\",\n" +
				"  \"goalId\": goalID\n" +
				"}";
		certAddRequestString = certAddRequestString.replace("goalID",manyTimeGoal.getId()+"");
		ManyTimeCertificationRequest certificationRequest = new ManyTimeCertificationRequest("ddd",new MockMultiPartFile(),manyTimeGoal.getId());
		ManyTimeCertification certification = new ManyTimeCertification(certificationRequest,manyTimeGoal);
		certification = manyTimeCertificationService.createCertification(certification,1);
		//감증 등록
		User user = credentialService.getUserById(2);
		Page<Certification> page = certificationGetService.getCertifications(0);
		String req = "{\n" +
				"  \"certificationId\": {certId},\n" +
				"  \"userId\": {userId},\n" +
				"  \"state\": true\n" +
				"}";
		req = req.replace("{certId}",certification.getId()+"").replace("{userId}",user.getId()+"");
		VerificationRequest verificationRequest = objectMapper.readValue(req, VerificationRequest.class);
		int beforePoint = user.getPoint();
		int beforeCertSuccessCount = certification.getSuccessCount();
		verificationService.createVerification(verificationRequest,user.getId());
		int afterPoint = user.getPoint();
		int afterCertSuccessCount = certification.getSuccessCount();
		assertThat(beforePoint).isEqualTo(afterPoint-100);
		assertThat(beforeCertSuccessCount).isEqualTo(afterCertSuccessCount-1);
	}
	@Test
	@Transactional
	void verificationTestWithChangeCertificationStatus() throws JsonProcessingException {
		Certification certification = certificationGetService.getCertificationById(1);
		GoalState beforeGoalState =  certification.getGoal().getGoalState();
		CertificationState before = certification.getState();
		String request = "{\n" +
				"  \"certificationId\": 1,\n" +
				"  \"userId\": 2,\n" +
				"  \"state\": false\n" +
				"}";
		VerificationRequest verificationRequest = objectMapper.readValue(request,VerificationRequest.class);
		int beforePoint = credentialService.getUserById(2).getPoint();
		verificationService.createVerification(verificationRequest,2);
		int afterPoint = credentialService.getUserById(2).getPoint();

		GoalState afterGoalState =  certification.getGoal().getGoalState();
		CertificationState after = certification.getState();
		assertThat(beforePoint).isEqualTo(afterPoint-100);
		assertThat(before).isEqualTo(CertificationState.ONGOING);
		assertThat(after).isEqualTo(CertificationState.FAIL);
		assertThat(beforeGoalState).isEqualTo(GoalState.ONGOING);
		assertThat(afterGoalState).isEqualTo(GoalState.FAIL);
	}
}

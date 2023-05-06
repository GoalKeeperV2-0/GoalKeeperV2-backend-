package kr.co.goalkeeper.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import kr.co.goalkeeper.api.model.entity.*;
import kr.co.goalkeeper.api.model.entity.goal.*;
import kr.co.goalkeeper.api.model.request.*;
import kr.co.goalkeeper.api.model.response.*;
import kr.co.goalkeeper.api.service.port.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
class GoalkeeperApplicationTests {
	@Autowired
	ApplicationContext applicationContext;
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
	HoldGoalService holdGoalService;
	@Autowired
	VerificationService verificationService;
	@Autowired
	NotificationService notificationService;
	@BeforeEach
	void init(){
		NotificationSender.init(applicationContext);
	}

	/**
	 * sns 회원가입 후 추가정보 입력 테스트
	 */
	@Test
	@Transactional
	void joinCompleteTest() throws JsonProcessingException {
		ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
		String adduserInfo = "{\n" +
				"  \"nickName\": \"robin\",\n" +
				"  \"age\": 26,\n" +
				"  \"sex\": \"MAN\"\n" +
				"}";
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
		Page<GoalResponse> goals = goalGetService.getGoalsByUserId(1,0);
		List<GoalResponse> goalList = goals.getContent();
		assertThat(goalList.size()).isEqualTo(2);
		assertThat(goalList.get(0).getClass()).isEqualTo(ManyTimeGoalResponse.class);
		assertThat(goalList.get(1).getClass()).isEqualTo(OneTimeGoalResponse.class);
	}

	/**
	 * 목표 등록 테스트
	 */
	@Test
	@Transactional
	void goalAddTest(){
		OneTimeGoalRequest goalRequest = OneTimeGoalRequest.getTestInstance(CategoryType.STUDY, 100,Reward.HIGH_RETURN,LocalDate.of(2026,8,4));
		User user = credentialService.getUserById(1);
		OneTimeGoal oneTimeGoal = new OneTimeGoal(goalRequest,user);
		OneTimeGoal added = oneTimeGoalService.createOneTimeGoal(oneTimeGoal);
		assertThat(added).isEqualTo(oneTimeGoal);
		Page<GoalResponse> goalPage = goalGetService.getGoalsByUserId(1,0);
		assertThat(goalPage.stream().anyMatch(goal -> goal.getId() == added.getId())).isEqualTo(true);

		//지속 목표 등록
		List<LocalDate> certDates = new ArrayList<>();
		for (int i = 0; i < 6; i++) {
			certDates.add(LocalDate.now().plusDays(i));
		}
		ManyTimeGoalRequest manyTimeGoalRequest = ManyTimeGoalRequest.getTestInstance(CategoryType.STUDY,200,Reward.HIGH_RETURN,
				LocalDate.now().plusDays(5),certDates);
		User user1 = credentialService.getUserById(1);
		ManyTimeGoal manyTimeGoal = new ManyTimeGoal(manyTimeGoalRequest,user1);
		ManyTimeGoal addedMany = manyTimeGoalService.createManyTimeGoal(manyTimeGoal);
		assertThat(addedMany).isEqualTo(manyTimeGoal);
		goalPage = goalGetService.getGoalsByUserId(1,0);
		assertThat(goalPage.stream().anyMatch(goal -> goal.getId() == addedMany.getId())).isEqualTo(true);

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
	private void addManyGoalFail1(){
		List<LocalDate> certDates = new ArrayList<>();
		for (int i = 0; i < 6; i++) {
			certDates.add(LocalDate.now().plusDays(i));
		}
		ManyTimeGoalRequest manyTimeGoalRequest = ManyTimeGoalRequest.getTestInstance(CategoryType.STUDY,200,Reward.HIGH_RETURN,
				LocalDate.now().minusDays(1),certDates);
		User user1 = credentialService.getUserById(1);
		ManyTimeGoal manyTimeGoal = new ManyTimeGoal(manyTimeGoalRequest,user1);
		try {
			manyTimeGoalService.createManyTimeGoal(manyTimeGoal);
		}catch (Exception e){
			assertThat(e).hasMessage("목표 시작날짜는 목표 종료날짜보다 4일 이상 빨라야 합니다.\n목표 시작날짜와 목표 종료날짜는 오늘보다 빠르면 안됩니다.");
		}
	}
	private void addManyGoalFail1_1(){
		List<LocalDate> certDates = new ArrayList<>();
		for (int i = 0; i < 3; i++) {
			certDates.add(LocalDate.now().plusDays(i));
		}
		ManyTimeGoalRequest manyTimeGoalRequest = ManyTimeGoalRequest.getTestInstance(CategoryType.STUDY,200,Reward.HIGH_RETURN,
				LocalDate.now().plusDays(3),certDates);
		User user1 = credentialService.getUserById(1);
		ManyTimeGoal manyTimeGoal = new ManyTimeGoal(manyTimeGoalRequest,user1);
		try {
			manyTimeGoalService.createManyTimeGoal(manyTimeGoal);
		}catch (Exception e){
			assertThat(e).hasMessage("목표 시작날짜는 목표 종료날짜보다 4일 이상 빨라야 합니다.\n목표 시작날짜와 목표 종료날짜는 오늘보다 빠르면 안됩니다.");
		}
	}
	private void addManyGoalFail2(){
		List<LocalDate> certDates = new ArrayList<>();
		for (int i = 0; i < 6; i++) {
			certDates.add(LocalDate.now().plusDays(i));
		}
		ManyTimeGoalRequest manyTimeGoalRequest = ManyTimeGoalRequest.getTestInstance(CategoryType.STUDY,200000,Reward.HIGH_RETURN,
				LocalDate.now().plusDays(5),certDates);
		User user2 = credentialService.getUserById(2);
		ManyTimeGoal manyTimeGoal = new ManyTimeGoal(manyTimeGoalRequest,user2);
		try {
			manyTimeGoalService.createManyTimeGoal(manyTimeGoal);
		}catch (Exception e){
			assertThat(e).hasMessage("포인트가 부족 합니다.");
		}
	}
	private void addManyGoalFail3(){
		List<LocalDate> certDates = new ArrayList<>();
		for (int i = 0; i < 5; i++) {
			certDates.add(LocalDate.now().plusDays(i));
		}
		certDates.add(LocalDate.now().plusDays(6));
		ManyTimeGoalRequest manyTimeGoalRequest = ManyTimeGoalRequest.getTestInstance(CategoryType.STUDY,200,Reward.HIGH_RETURN,
				LocalDate.now().plusDays(5),certDates);
		User user1 = credentialService.getUserById(1);
		ManyTimeGoal manyTimeGoal = new ManyTimeGoal(manyTimeGoalRequest,user1);
		try {
			manyTimeGoalService.createManyTimeGoal(manyTimeGoal);
		}catch (Exception e){
			assertThat(e).hasMessage("인증날은 [시작날,마지막날] 구간에 있어야 합니다.");
		}
	}
	private void addManyGoalFail4(){
		List<LocalDate> certDates = new ArrayList<>();
		certDates.add(LocalDate.now());
		certDates.add(LocalDate.now().plusDays(1));
		certDates.add(LocalDate.now().plusDays(5));
		ManyTimeGoalRequest manyTimeGoalRequest = ManyTimeGoalRequest.getTestInstance(CategoryType.STUDY,200, Reward.HIGH_RETURN,
				LocalDate.now().plusDays(5),certDates);
		User user1 = credentialService.getUserById(1);
		ManyTimeGoal manyTimeGoal = new ManyTimeGoal(manyTimeGoalRequest,user1);
		try {
			manyTimeGoalService.createManyTimeGoal(manyTimeGoal);
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
		Page<CertificationResponse> page = certificationGetService.getCertifications(0,0).getCertificationResponses();
		assertThat(page.getTotalElements()).isEqualTo(2);
		assertThat(page.getContent().get(0).getClass()).isEqualTo(ManyTimeCertificationResponse.class);
		assertThat(page.getContent().get(1).getClass()).isEqualTo(OneTimeCertificationResponse.class);
	}

	/**
	 * 인증 등록 테스트
	 */
	@Test
	@Transactional
	void certAddTest(){
		// 지속목표 등록
		List<LocalDate> certDates = new ArrayList<>();
		for (int i = 0; i < 6; i++) {
			certDates.add(LocalDate.now().plusDays(i));
		}
		ManyTimeGoalRequest manyTimeGoalRequest = ManyTimeGoalRequest.getTestInstance(CategoryType.STUDY,200,Reward.HIGH_RETURN,
				LocalDate.now().plusDays(5),certDates);
		User user1 = credentialService.getUserById(1);
		ManyTimeGoal manyTimeGoal = new ManyTimeGoal(manyTimeGoalRequest,user1);
		manyTimeGoal = manyTimeGoalService.createManyTimeGoal(manyTimeGoal);
		//인증 등록
		ManyTimeCertificationRequest certificationRequest = ManyTimeCertificationRequest.getTestInstance("ddd", new MockMultiPartFile(),manyTimeGoal.getId());
		ManyTimeCertification certification = new ManyTimeCertification(certificationRequest,manyTimeGoal);
		certification = manyTimeCertificationService.createCertification(certification,1);

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
		Page<CertificationResponse> page = certificationGetService.getCertifications(0,0).getCertificationResponses();
		assertThat(page.getTotalElements()).isEqualTo(3);
		assertThat(certification.getGoal().getGoalState()).isEqualTo(GoalState.ONGOING);

		// 일반 목표 등록
		OneTimeGoalRequest oneTimeGoalRequest = OneTimeGoalRequest.getTestInstance(CategoryType.STUDY, 2000,Reward.HIGH_RETURN,LocalDate.of(2026,8,4));
		OneTimeGoal oneTimeGoal = new OneTimeGoal(oneTimeGoalRequest,user1);
		oneTimeGoal = oneTimeGoalService.createOneTimeGoal(oneTimeGoal);
		// 일반 목표 인증 등록
		OnetimeCertificationRequest onetimeCertificationRequest = OnetimeCertificationRequest.getTestInstance("ttt", new MockMultiPartFile(),oneTimeGoal.getId());
		OneTimeCertification oneTimeCertification = new OneTimeCertification(onetimeCertificationRequest,oneTimeGoal);
		oneTimeCertificationService.createCertification(oneTimeCertification,user1.getId());
		assertThat(oneTimeGoal.getGoalState()).isEqualTo(GoalState.WAITING_CERT_COMPLETE);
		// 예외 발생할 요청 - 이미 인증이 등록된 경우
		try{
			oneTimeCertificationService.createCertification(oneTimeCertification,user1.getId());
		}catch (Exception e){
			assertThat(e).hasMessage("이미 인증이 등록된 목표입니다.");
		}
		// Todo 예외 발생한 경우 - 진행중이 아닌 목표에 인증을 등록할 경우
	}
	static class MockMultiPartFile implements MultipartFile{

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
		public byte[] getBytes(){
			return new byte[0];
		}

		@Override
		public InputStream getInputStream(){
			return null;
		}

		@Override
		public void transferTo(File dest){
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
	void VerificationTest(){
		// 지속목표 등록
		List<LocalDate> certDates = new ArrayList<>();
		for (int i = 0; i < 6; i++) {
			certDates.add(LocalDate.now().plusDays(i));
		}
		ManyTimeGoalRequest goalRequest = ManyTimeGoalRequest.getTestInstance(CategoryType.STUDY,200,Reward.HIGH_RETURN,
				LocalDate.now().plusDays(5),certDates);
		User user1 = credentialService.getUserById(1);
		ManyTimeGoal manyTimeGoal = new ManyTimeGoal(goalRequest,user1);
		manyTimeGoal = manyTimeGoalService.createManyTimeGoal(manyTimeGoal);
		//인증 등록
		ManyTimeCertificationRequest certificationRequest = ManyTimeCertificationRequest.getTestInstance("ddd", new MockMultiPartFile(),manyTimeGoal.getId());
		ManyTimeCertification certification = new ManyTimeCertification(certificationRequest,manyTimeGoal);
		certification = manyTimeCertificationService.createCertification(certification,1);
		//감증 등록
		User user = credentialService.getUserById(2);
		VerificationRequest verificationRequest = VerificationRequest.getTestInstance(certification.getId(),true);
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
	void verificationTestWithChangeCertificationStatus(){
		Certification certification = certificationGetService.getCertificationById(1);
		GoalState beforeGoalState =  certification.getGoal().getGoalState();
		CertificationState before = certification.getState();
		VerificationRequest verificationRequest = VerificationRequest.getTestInstance(1,false);
		int beforePoint = credentialService.getUserById(2).getPoint();
		verificationService.createVerification(verificationRequest,2);
		int afterPoint = credentialService.getUserById(2).getPoint();

		GoalState afterGoalState =  certification.getGoal().getGoalState();
		CertificationState after = certification.getState();
		assertThat(beforePoint).isEqualTo(afterPoint-100);
		assertThat(before).isEqualTo(CertificationState.ONGOING);
		assertThat(after).isEqualTo(CertificationState.FAIL);
		assertThat(beforeGoalState).isEqualTo(GoalState.WAITING_CERT_COMPLETE);
		assertThat(afterGoalState).isEqualTo(GoalState.FAIL);
	}
	@Test
	@Transactional
	void goalHoldTest(){
		Goal goal = goalGetService.getGoalById(1);
		User user1 = credentialService.getUserById(1);
		User user2 = credentialService.getUserById(2);
		VerificationRequest verificationRequest = VerificationRequest.getTestInstance(1,false);
		verificationService.createVerification(verificationRequest,user2.getId());
		holdGoalService.holdGoal(user1,goal.getId());
		assertThat(goal.getGoalState()).isEqualTo(GoalState.HOLD);
	}

	@Test
	@Transactional
	void notificationTest(){
		Notification notification = Notification.builder()
				.notificationType(NotificationType.GOAL_ADD)
				.content("test")
				.isRead(false)
				.createdDate(LocalDate.now())
				.receiver(credentialService.getUserById(1))
				.build();
		notificationService.sendNotification(notification);
		Slice<Notification> result = notificationService.getNotifications(1, Pageable.ofSize(10));
		assertThat(result.getContent().get(0)).isEqualTo(notification);
	}

	@Test
	@Transactional
	void manyTimeGoalStateTest(){
		List<LocalDate> certDates = new ArrayList<>();
		for (int i = 0; i < 6; i++) {
			certDates.add(LocalDate.now().plusDays(i));
		}
		ManyTimeGoalRequest manyTimeGoalRequest = ManyTimeGoalRequest.getTestInstance(CategoryType.STUDY,200,Reward.HIGH_RETURN,
				LocalDate.now().plusDays(5),certDates);
		User user1 = credentialService.getUserById(1);
		ManyTimeGoal manyTimeGoal = new ManyTimeGoal(manyTimeGoalRequest,user1);
		manyTimeGoal = manyTimeGoalService.createManyTimeGoal(manyTimeGoal);
		var manyTimeCertifications = new ArrayList<ManyTimeCertification>();
		User user2 = credentialService.getUserById(2);

		/* 	지속목표의 각 날짜별 인증 생성 단, 마지막날 인증은 제외.
			이 때 각 인증은 한번만 더 성공 검증 받으면 성공으로 바뀌는 상태
		 */

		for (int i = 0; i < 5; i++) {
			GoalState before = manyTimeGoal.getGoalState();
			manyTimeCertifications.add(manyTimeCertificationService.createCertification(ManyTimeCertification.getTestInstance(4,0,manyTimeGoal, new MockMultiPartFile(),LocalDate.now().plusDays(i)),1));
			GoalState after = manyTimeGoal.getGoalState();
			assertThat(before).isEqualTo(GoalState.ONGOING);
			assertThat(after).isEqualTo(GoalState.ONGOING);
		}

		// 지속목표의 마지막 인증 등록. 이 인증은 한번만 더 성공 검증 받으면 성공으로 바뀌는 상태
		GoalState before = manyTimeGoal.getGoalState();
		ManyTimeCertification last = ManyTimeCertification.getTestInstance(4,0,manyTimeGoal, new MockMultiPartFile(),LocalDate.now().plusDays(5));
		last = manyTimeCertificationService.createCertification(last,1);
		manyTimeCertifications.add(last);
		GoalState after = manyTimeGoal.getGoalState();
		assertThat(before).isEqualTo(GoalState.ONGOING);
		assertThat(after).isEqualTo(GoalState.WAITING_CERT_COMPLETE);

		// 각 인증이 성공판정을 받았을 때 인증이 성공이 되는지, 연관된 목표의 상태가 바뀌는지, 포인트 지급이 제대로 이뤄지는지 테스트
		long user1BeforePoint = user1.getPoint();
		for (int i = 0; i < manyTimeCertifications.size(); i++) {
			ManyTimeCertification manyTimeCertification = manyTimeCertifications.get(i);
			CertificationState beforeState = manyTimeCertification.getState();
			VerificationRequest verificationRequest = VerificationRequest.getTestInstance(manyTimeCertification.getId(), true);
			verificationService.createVerification(verificationRequest, user2.getId());
			CertificationState afterState = manyTimeCertification.getState();
			assertThat(beforeState).isEqualTo(CertificationState.ONGOING);
			assertThat(afterState).isEqualTo(CertificationState.SUCCESS);
			if(i==4){
				assertThat(manyTimeGoal.getGoalState()).isEqualTo(GoalState.SUCCESS);
				long user1AfterPoint = user1.getPoint();
				assertThat(user1AfterPoint - user1BeforePoint).isEqualTo(300);
			}
			if(i==5){
				long user1AfterPoint = user1.getPoint();
				assertThat(user1AfterPoint - user1BeforePoint).isEqualTo(380);
			}
		}

	}
}
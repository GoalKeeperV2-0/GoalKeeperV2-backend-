package kr.co.goalkeeper.api;

import kr.co.goalkeeper.api.model.entity.Certification;
import kr.co.goalkeeper.api.model.entity.OneTimeCertification;
import kr.co.goalkeeper.api.model.entity.User;
import kr.co.goalkeeper.api.model.entity.Verification;
import kr.co.goalkeeper.api.repository.*;
import kr.co.goalkeeper.api.service.OneTimeCertificationService;
import kr.co.goalkeeper.api.service.VerificationService;
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

	@Test
	@Transactional
	@Rollback(value = false)
	void contextLoads() {
//		OneTimeCertification certification = oneTimeCertificationService.getCertificationByGoalId(3);
//		User user = userRepository.findById(2L).get();
//		Verification verification = new Verification(certification,user,true);
//		//oneTimeCertificationRepository.save(certification);
//		verificationRepository.save(verification);
	}

}

package kr.co.goalkeeper.api.controller;

import kr.co.goalkeeper.api.model.entity.*;
import kr.co.goalkeeper.api.model.entity.goal.*;
import kr.co.goalkeeper.api.model.request.*;
import kr.co.goalkeeper.api.model.response.CertificationPageResponse;
import kr.co.goalkeeper.api.model.response.CertificationResponse;
import kr.co.goalkeeper.api.model.response.Response;
import kr.co.goalkeeper.api.repository.UserRepository;
import kr.co.goalkeeper.api.service.port.*;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static kr.co.goalkeeper.api.controller.TestController.RandomStringGenerator.*;

@RestController
@RequestMapping("/api/test")
@AllArgsConstructor
public class TestController {
    private final CertificationGetService certificationGetService;
    private final ManyTimeCertificationService manyTimeCertificationService;
    private final OneTimeCertificationService oneTimeCertificationService;
    private final GoalGetService goalGetService;
    private final CredentialService credentialService;
    private final ManyTimeGoalService manyTimeGoalService;
    private final OneTimeGoalService oneTimeGoalService;
    private final HoldGoalService holdGoalService;
    private final VerificationService verificationService;

    private final UserRepository userRepository;
    private final CategoryService categoryService;

    @PostMapping("")
    public ResponseEntity<Response<String>> makeTestData(){
        List<Category> categoryList = categoryService.getCategoryList();
        List<User> users = User.getTestInstances(categoryList,20);
        userRepository.saveAll(users);
        for (User user: users) {
            List<LocalDate> certDates = new ArrayList<>();
            for (int i = 0; i < 6; i++) {
                certDates.add(LocalDate.now().plusDays(i));
            }
            String content = generateRandomString(20);
            String title = generateRandomString(10);
            ManyTimeGoalRequest manyTimeGoalRequest = ManyTimeGoalRequest.getTestInstance(title,content,CategoryType.STUDY,100, Reward.HIGH_RETURN, LocalDate.now().plusDays(5),certDates);
            ManyTimeGoal manyTimeGoal = new ManyTimeGoal(manyTimeGoalRequest,user);
            manyTimeGoal = manyTimeGoalService.createManyTimeGoal(manyTimeGoal);
            for (LocalDate certDate: certDates) {
                content = generateRandomString(20);
                ManyTimeCertification manyTimeCertification = ManyTimeCertification.getTestInstance(content,manyTimeGoal,null,certDate);
                manyTimeCertificationService.createCertification(manyTimeCertification,user.getId());
            }
            content = generateRandomString(20);
            title = generateRandomString(10);
            OneTimeGoalRequest oneTimeGoalRequest = OneTimeGoalRequest.getTestInstance(title,content,CategoryType.EXERCISE,100, Reward.LOW_RETURN,LocalDate.now());
            OneTimeGoal oneTimeGoal = new OneTimeGoal(oneTimeGoalRequest,user);
            oneTimeGoal = oneTimeGoalService.createOneTimeGoal(oneTimeGoal);
            content = generateRandomString(20);
            OnetimeCertificationRequest onetimeCertificationRequest = OnetimeCertificationRequest.getTestInstance(content,null,oneTimeGoal.getId());
            OneTimeCertification oneTimeCertification =new OneTimeCertification(onetimeCertificationRequest,oneTimeGoal);
            oneTimeCertificationService.createCertification(oneTimeCertification,user.getId());
        }
        for (User user:users) {
            int pageNumber = 0;
            CertificationPageResponse certificationPageResponse = certificationGetService.getCertifications(user.getId(),pageNumber);
            Page<CertificationResponse> page = certificationPageResponse.getCertificationResponses();
            addVerification(user, certificationPageResponse, page);
            while (page.hasNext()){
                certificationPageResponse = certificationGetService.getCertifications(user.getId(),++pageNumber);
                page = certificationPageResponse.getCertificationResponses();
                addVerification(user, certificationPageResponse, page);
            }
        }
        return null;
    }

    private void addVerification(User user, CertificationPageResponse certificationPageResponse, Page<CertificationResponse> page) {
        List<Long> alreadyVerification1 = certificationPageResponse.getAlreadyVerification();
        page.forEach(c -> {
            if(alreadyVerification1.contains(c.getId())){
                return;
            }
            if(c.getId()%2==0){
                return;
            }
            boolean state = new Random().nextInt()%2==0;
            VerificationRequest verificationRequest = VerificationRequest.getTestInstance(c.getId(),state);
            verificationService.createVerification(verificationRequest,user.getId());
        });
    }

    static class RandomStringGenerator {
        private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

        public static String generateRandomString(int LENGTH) {
            Random random = new Random();
            StringBuilder sb = new StringBuilder(LENGTH);
            for (int i = 0; i < LENGTH; i++) {
                int index = random.nextInt(CHARACTERS.length());
                sb.append(CHARACTERS.charAt(index));
            }
            return sb.toString();
        }
    }
}

package kr.co.goalkeeper.api.model.entity;

import kr.co.goalkeeper.api.exception.GoalkeeperException;
import kr.co.goalkeeper.api.model.request.OnetimeCertificationRequest;
import kr.co.goalkeeper.api.model.response.ErrorMessage;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.io.File;
import java.time.LocalDate;

@Entity
@DiscriminatorValue("OneTimeCertification")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OneTimeCertification extends Certification {
    public OneTimeCertification(OnetimeCertificationRequest dto,Goal goal){
        content = dto.getContent();
        state = CertificationState.ONGOING;
        date = LocalDate.now();
        this.goal = goal;
        picture = makePicturePath(dto.getPicture(),goal.getId());
        pictureFile = dto.getPicture();
    }
    private String makePicturePath(MultipartFile multipartFile,long goalId){
        return pictureRootPath + File.pathSeparator+goalId+File.pathSeparator+date.toString() + getFileExtension(multipartFile);
    }
    private String getFileExtension(MultipartFile multipartFile){
        String fileName = multipartFile.getOriginalFilename();
        if(!fileName.contains(".")){
            ErrorMessage errorMessage = new ErrorMessage(400,"이미지 파일이 아닙니다.");
            throw new GoalkeeperException(errorMessage);
        }
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }
    @Override
    public void verificationSuccess() {
        int requiredSuccessCount = goal.requiredSuccessCount();
        successCount++;
        if(successCount>=requiredSuccessCount){
            success();
        }
    }

    @Override
    public void verificationFail() {
        int requiredSuccessCount = goal.requiredSuccessCount();
        failCount++;
        if(failCount>=Math.round(0.7 * requiredSuccessCount)){ // 인증 상태가 실패로 바뀌어야 되는 경우
            fail();
        }
    }
    @Override
    protected void success() {
        super.success();
        goal.success();
    }

    @Override
    protected void fail() {
        super.fail();
        goal.failFromOngoing();
    }
}

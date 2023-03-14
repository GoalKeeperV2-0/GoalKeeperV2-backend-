package kr.co.goalkeeper.api.util;

import kr.co.goalkeeper.api.exception.GoalkeeperException;
import kr.co.goalkeeper.api.model.entity.Certification;
import kr.co.goalkeeper.api.model.response.ErrorMessage;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;

public class ImageSaver {

    public static String saveProfileImage(String pictureRootPath,MultipartFile image, long userId){
        String profileRootPath = pictureRootPath + File.separator+"profiles"+ File.separator;
        File directory = new File(profileRootPath);
        if(!directory.exists()){
            directory.mkdirs();
        }
        File file = new File(profileRootPath+userId+"."+getFileExtension(image));
        try {
            image.transferTo(file);
        }catch (IOException e){
            ErrorMessage errorMessage = new ErrorMessage(500,"프로필 이미지 저장에 실패했습니다");
            throw new GoalkeeperException(errorMessage);
        }
        return profileRootPath+userId+"."+getFileExtension(image);
    }
    private static String getFileExtension(MultipartFile multipartFile){
        String fileName = multipartFile.getOriginalFilename();
        if(!fileName.contains(".")){
            ErrorMessage errorMessage = new ErrorMessage(400,"이미지 파일이 아닙니다.");
            throw new GoalkeeperException(errorMessage);
        }
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }
    public static void saveCertificationPicture(Certification certification,String pictureRootPath){
        try {
            certification.setPicture(makePicturePath(certification,pictureRootPath));
            String filePath = certification.getPicture();
            MultipartFile pictureFile = certification.getPictureFile();
            if(pictureFile == null) {
                return;
            }
            File file = new File(filePath);
            pictureFile.transferTo(file);
        }catch (Exception e){
            ErrorMessage errorMessage = new ErrorMessage(500,"인증 이미지 저장에 실패했습니다.");
            throw new GoalkeeperException(errorMessage);
        }
    }
    private static String makePicturePath(Certification certification,String pictureRootPath){
        MultipartFile multipartFile = certification.getPictureFile();
        if(multipartFile == null){
            return pictureRootPath+File.separator+"err.png";
        }
        long goalId = certification.getGoal().getId();
        String directoryPath = pictureRootPath + File.separator+goalId;
        File directory = new File(directoryPath);
        if(!directory.exists()){
            directory.mkdirs();
        }
        return pictureRootPath + File.separator+goalId+File.separator+ LocalDate.now() +"."+ getFileExtension(multipartFile);
    }
}

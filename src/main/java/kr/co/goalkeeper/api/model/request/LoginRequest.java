package kr.co.goalkeeper.api.model.request;


import kr.co.goalkeeper.api.exception.GoalkeeperException;
import kr.co.goalkeeper.api.model.response.ErrorMessage;

import javax.validation.constraints.Email;
public class LoginRequest {
    @Email
    private String email;
    private String password;

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        String message ="";
        if(!isValidLength(password)){
            message+="비밀번호는 최소 8자 이상이어야 합니다.";
        }
        if(!hasLetterAndDigit(password)){
            message+="비밀번호에는 알파벳 대소문자나 숫자가 포함되어야 합니다.";
        }
        if(!hasNoWhitespace(password)){
            message+="비밀번호에는 공백이 포함되어선 안됩니다.";
        }
        if(!hasSpecialCharacter(password)){
            message+="비밀번호에는 특수문자가 포함되어야 합니다.";
        }
        if(!message.isEmpty()){
            ErrorMessage errorMessage = new ErrorMessage(400,message);
            throw new GoalkeeperException(errorMessage);
        }
        return password;
    }
    private boolean isValidLength(String password) {
        return password.length() >= 8;
    }
    private boolean hasLetterAndDigit(String password) {
        boolean hasLetter = false;
        boolean hasDigit = false;
        for (char c : password.toCharArray()) {
            if (Character.isLetter(c)) {
                hasLetter = true;
            } else if (Character.isDigit(c)) {
                hasDigit = true;
            }
            if (hasLetter && hasDigit) {
                break;
            }
        }
        return hasLetter && hasDigit;
    }
    private boolean hasSpecialCharacter(String password) {
        for (char c : password.toCharArray()) {
            if (!Character.isLetterOrDigit(c)) {
                return true;
            }
        }
        return false;
    }
    private boolean hasNoWhitespace(String password) {
        return !password.contains(" ");
    }
}

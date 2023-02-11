package kr.co.goalkeeper.api.model.request;


import kr.co.goalkeeper.api.exception.GoalkeeperException;
import kr.co.goalkeeper.api.model.response.ErrorMessage;
import kr.co.goalkeeper.api.util.PasswordManager;

import javax.validation.constraints.Email;
public class LoginRequest {
    @Email
    private String email;
    private String password;

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        String message = PasswordManager.validatePassword(password);
        if(!message.isEmpty()){
            ErrorMessage errorMessage = new ErrorMessage(400,message);
            throw new GoalkeeperException(errorMessage);
        }
        return password;
    }
}
package kr.co.goalkeeper.api.model.request;

import kr.co.goalkeeper.api.exception.GoalkeeperException;
import kr.co.goalkeeper.api.model.entity.Sex;
import kr.co.goalkeeper.api.model.response.ErrorMessage;
import kr.co.goalkeeper.api.util.PasswordManager;
import lombok.Getter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
public class JoinRequest {
    @Email
    private String email;
    @NotBlank
    private String name;
    @NotNull
    private String password;
    private Sex sex;
    private int age;
    private String picture;
    public String getPassword() {
        String message = PasswordManager.validatePassword(password);
        if (!message.isEmpty()) {
            ErrorMessage errorMessage = new ErrorMessage(400, message);
            throw new GoalkeeperException(errorMessage);
        }
        return password;
    }
}

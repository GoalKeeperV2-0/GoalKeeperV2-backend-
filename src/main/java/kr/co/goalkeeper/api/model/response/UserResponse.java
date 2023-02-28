package kr.co.goalkeeper.api.model.response;

import kr.co.goalkeeper.api.model.entity.Sex;
import kr.co.goalkeeper.api.model.entity.User;
import lombok.Getter;

@Getter
public class UserResponse {
    private final String email;
    private final String picture;
    private final String name;
    private final Sex sex;
    private final Integer age;
    public UserResponse(User entity){
        email = entity.getEmail();
        if(entity.getPicture()!=null&&!entity.getPicture().isBlank()) {
            if(entity.getPicture().contains("http")){
                picture = entity.getPicture();
            }else {
                picture = "/api/image/user/" + entity.getId();
            }
        }else {
            picture = null;
        }
        name = entity.getName();
        sex = entity.getSex();
        age = entity.getAge();
    }
}

package kr.co.goalkeeper.api.model.request;

import kr.co.goalkeeper.api.model.entity.Sex;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;
@Getter
@Setter
public class UpdateUserRequest {
    private MultipartFile picture;
    private String name;
    private Sex sex;
    private Integer age;
}

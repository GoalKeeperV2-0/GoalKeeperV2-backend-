package kr.co.goalkeeper.api.model.request;

import kr.co.goalkeeper.api.model.entity.Sex;
import lombok.Getter;

@Getter
public class AdditionalUserInfo {
    private String nickName;
    private int age;
    private Sex sex;
}

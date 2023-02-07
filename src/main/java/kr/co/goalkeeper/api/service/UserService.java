package kr.co.goalkeeper.api.service;

import kr.co.goalkeeper.api.model.entity.User;
import kr.co.goalkeeper.api.model.request.AdditionalUserInfo;

public interface UserService {
    void addUser(User user);
    boolean isAlreadyRegistered(String email);
    User getUserByEmail(String email);
    User getUserById(long id);
    void completeJoin(User user, AdditionalUserInfo userInfo);
}

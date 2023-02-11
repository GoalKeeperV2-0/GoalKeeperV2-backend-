package kr.co.goalkeeper.api.service;

import kr.co.goalkeeper.api.model.entity.User;
import kr.co.goalkeeper.api.model.request.AdditionalUserInfo;
import kr.co.goalkeeper.api.model.request.LoginRequest;

public interface UserService {
    void addUser(User user);
    boolean isAlreadyRegistered(String email);
    User getUserByEmail(String email);
    User getUserById(long id);
    User getUserByEmailAndPassword(String email,String password);
    void completeJoin(User user, AdditionalUserInfo userInfo);
}

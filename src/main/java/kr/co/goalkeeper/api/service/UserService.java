package kr.co.goalkeeper.api.service;

import kr.co.goalkeeper.api.model.User;

public interface UserService {
    void addUser(User user);
    boolean isAlreadyRegistered(String email);
    User getUserByEmail(String email);
    User getUserById(long id);
}

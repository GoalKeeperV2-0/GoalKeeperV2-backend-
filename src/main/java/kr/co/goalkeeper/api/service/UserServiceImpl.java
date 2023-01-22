package kr.co.goalkeeper.api.service;

import kr.co.goalkeeper.api.model.domain.AdditionalUserInfo;
import kr.co.goalkeeper.api.model.domain.User;
import kr.co.goalkeeper.api.repository.UserRepository;
import org.springframework.stereotype.Service;

import static kr.co.goalkeeper.api.model.domain.User.EMPTYUSER;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void addUser(User user) {
        userRepository.save(user);
    }

    @Override
    public boolean isAlreadyRegistered(String email) {
        User user = userRepository.findByEmail(email).orElse(EMPTYUSER);
        return !user.equals(EMPTYUSER);
    }

    @Override
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow();
    }

    @Override
    public User getUserById(long id) {
        return userRepository.findById(id).orElseThrow();
    }

    @Override
    public void completeJoin(User user, AdditionalUserInfo userInfo) {
        if(!user.isJoinComplete()){
            user.setAdditional(userInfo);
            user.joinComplete();
        }
        userRepository.save(user);
    }
}

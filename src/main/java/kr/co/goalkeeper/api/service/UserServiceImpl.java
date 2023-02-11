package kr.co.goalkeeper.api.service;

import kr.co.goalkeeper.api.exception.GoalkeeperException;
import kr.co.goalkeeper.api.model.entity.User;
import kr.co.goalkeeper.api.model.request.AdditionalUserInfo;
import kr.co.goalkeeper.api.model.request.LoginRequest;
import kr.co.goalkeeper.api.model.response.ErrorMessage;
import kr.co.goalkeeper.api.repository.UserRepository;
import org.springframework.stereotype.Service;

import static kr.co.goalkeeper.api.model.entity.User.EMPTYUSER;

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
        return userRepository.findByEmail(email).orElseThrow(()->{
            ErrorMessage errorMessage = new ErrorMessage(404,"이메일이 잘못되었습니다.");
            return new GoalkeeperException(errorMessage);
        });
    }

    @Override
    public User getUserById(long id) {
        return userRepository.findById(id).orElseThrow();
    }

    @Override
    public User getUserByEmailAndPassword(String email,String password) {
        return userRepository.findByEmailAndPassword(email, password).orElseThrow(()->{
            ErrorMessage errorMessage = new ErrorMessage(404,"이메일이나 비밀번호가 틀렸습니다.");
            return new GoalkeeperException(errorMessage);
        });
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

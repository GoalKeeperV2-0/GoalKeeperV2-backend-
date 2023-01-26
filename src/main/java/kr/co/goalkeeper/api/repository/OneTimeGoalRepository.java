package kr.co.goalkeeper.api.repository;

import kr.co.goalkeeper.api.model.entity.Category;
import kr.co.goalkeeper.api.model.entity.OneTimeGoal;
import kr.co.goalkeeper.api.model.entity.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface OneTimeGoalRepository extends CrudRepository<OneTimeGoal,Long> {
    List<OneTimeGoal> findAllByUser(User user);
    List<OneTimeGoal> findAllByUserAndCategory(User user, Category category);
}

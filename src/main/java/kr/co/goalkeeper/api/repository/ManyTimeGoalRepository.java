package kr.co.goalkeeper.api.repository;

import kr.co.goalkeeper.api.model.domain.Category;
import kr.co.goalkeeper.api.model.domain.ManyTimeGoal;
import kr.co.goalkeeper.api.model.domain.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ManyTimeGoalRepository extends CrudRepository<ManyTimeGoal,Long> {
    List<ManyTimeGoal> findAllByUser(User user);
    List<ManyTimeGoal> findAllByUserAndCategory(User user, Category category);
}

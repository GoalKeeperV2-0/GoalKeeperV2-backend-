package kr.co.goalkeeper.api.repository;

import kr.co.goalkeeper.api.model.entity.Category;
import kr.co.goalkeeper.api.model.entity.OneTimeGoal;
import kr.co.goalkeeper.api.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OneTimeGoalRepository extends JpaRepository<OneTimeGoal,Long> {
    List<OneTimeGoal> findAllByUser(User user);
    List<OneTimeGoal> findAllByUserAndCategory(User user, Category category);
}

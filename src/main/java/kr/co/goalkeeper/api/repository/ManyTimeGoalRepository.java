package kr.co.goalkeeper.api.repository;

import kr.co.goalkeeper.api.model.entity.Category;
import kr.co.goalkeeper.api.model.entity.ManyTimeGoal;
import kr.co.goalkeeper.api.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ManyTimeGoalRepository extends JpaRepository<ManyTimeGoal,Long> {
    List<ManyTimeGoal> findAllByUser(User user);
    List<ManyTimeGoal> findAllByUserAndCategory(User user, Category category);
}

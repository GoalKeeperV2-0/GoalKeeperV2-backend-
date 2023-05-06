package kr.co.goalkeeper.api.repository;

import kr.co.goalkeeper.api.model.entity.CategoryType;
import kr.co.goalkeeper.api.model.entity.goal.Certification;
import kr.co.goalkeeper.api.model.entity.goal.CertificationState;
import kr.co.goalkeeper.api.model.entity.goal.GoalState;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Optional;
import java.util.Set;

public interface CertificationRepository extends JpaRepository<Certification,Long> {
    @EntityGraph(attributePaths = {"goal","goal.user"})
    Page<Certification> findAllByGoal_IdAndGoal_GoalStateIn(long goalId,Set<GoalState> goalStates,Pageable pageable);
    boolean existsByDateAndGoal_Id(LocalDate date,long goalId);
    @EntityGraph(attributePaths = {"goal","goal.user"})
    Page<Certification> findByGoal_Category_CategoryTypeAndStateAndGoal_User_IdNotLikeAndGoal_GoalStateIn(CategoryType goal_category_categoryType, @NotNull CertificationState state, long goal_user_id, Set<GoalState> goalStates, Pageable pageable);
    @EntityGraph(attributePaths = {"goal","goal.user"})
    Page<Certification> findByStateAndGoal_User_IdNotLikeAndGoal_GoalStateIn(CertificationState certificationState, long userId, Set<GoalState> goalStates, Pageable pageable);
    Set<Certification> findAllByGoal_IdIn(Set<Long> goalIds);
    @EntityGraph(attributePaths = "goal")
    @Override
    Optional<Certification> findById(Long aLong);
}

package kr.co.goalkeeper.api.repository;

import kr.co.goalkeeper.api.model.entity.*;
import kr.co.goalkeeper.api.model.entity.goal.Goal;
import kr.co.goalkeeper.api.model.entity.goal.GoalState;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface GoalRepository extends JpaRepository<Goal,Long> {
    @EntityGraph(attributePaths = "category")
    Page<Goal> findAllByUser_Id(long userId, Pageable pageable);
    @EntityGraph(attributePaths = "category")
    Page<Goal> findAllByUser_IdAndGoalState(long userId,GoalState goalState, Pageable pageable);
    @EntityGraph(attributePaths = "category")
    Page<Goal> findAllByUser_IdAndCategory_CategoryType(long userId, CategoryType categoryType, Pageable pageable);
    @EntityGraph(attributePaths = "category")
    Page<Goal> findAllByUser_IdAndCategory_CategoryTypeAndGoalState(long userId, CategoryType categoryType,GoalState goalState, Pageable pageable);
    long countAllByGoalState(GoalState goalState);
    long countAllByUser_Id(long user_id);
    long countAllByGoalStateAndUser_Id(GoalState goalState,long userId);
    @Query(value = "select id from((select g.id as id,g.start_date from goal g where g.end_date = CAST(:now as DATE) and g.id not in (select c.goal_id from certification c where c.date = CAST(:now as DATE)) and g.user_id =:userId and g.goal_state = 'ONGOING' order by g.start_date limit 2)union(select g.id as id,g.start_date from goal g join many_time_goal mtg on g.id = mtg.id join many_time_goal_cert_date mtgcd on g.id = mtgcd.goal_id where mtgcd.cert_date = CAST(:now as DATE) and g.user_id =:userId and g.id not in (select c.goal_id from certification c where c.date = CAST(:now as DATE)) and g.goal_state = 'ONGOING' order by g.start_date )order by start_date) as isdisd order by start_date limit 2",nativeQuery = true)
    List<Long> findGoalIdsByUSerIdAndNeedCertNow(LocalDate now, long userId);
    @EntityGraph(attributePaths = "category")
    @Override
    List<Goal> findAllById(Iterable<Long> longs);
}

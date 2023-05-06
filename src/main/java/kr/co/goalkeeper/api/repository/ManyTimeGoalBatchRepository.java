package kr.co.goalkeeper.api.repository;

import kr.co.goalkeeper.api.model.entity.goal.GoalState;
import kr.co.goalkeeper.api.model.entity.goal.ManyTimeGoal;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;

public interface ManyTimeGoalBatchRepository extends JpaRepository<ManyTimeGoal,Long> {
    Slice<ManyTimeGoal> findAllByGoalStateAndEndDate(GoalState goalState, LocalDate date, Pageable pageable);
    @Query(value = "select * from goal g join (select goal_id from many_time_goal_cert_date mtgcd where mtgcd.cert_date = CAST(:localDate as DATE)) as res on res.goal_id = g.id " +
            "join many_time_goal mtg on mtg.id = g.id where g.goal_state='ONGOING'",nativeQuery = true,countQuery = "select count(*) from goal g join (select goal_id from many_time_goal_cert_date mtgcd where mtgcd.cert_date = :localDate) as res on res.goal_id = g.id join many_time_goal mtg on mtg.id = g.id")
    Slice<ManyTimeGoal> findAllByCertDatesContaining(String localDate,Pageable pageable);
}

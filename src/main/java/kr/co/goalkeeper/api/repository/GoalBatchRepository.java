package kr.co.goalkeeper.api.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class GoalBatchRepository {
    private final JdbcTemplate jdbcTemplate;

    public GoalBatchRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * 목표 기간이 지나고, 목표가 진행중이며, 관련된 인증이 모두 인증이 완료된 경우 실행
     * 목표 상태를 fail 로 바꾸고, 포인트를 변경한다.
     */
    public void updateTimeOutGoal(){
        String sql = "update goal g join user u on g.user_id = u.id join user_category_point ucp on u.id = ucp.user_id set g.goal_state = 'FAIL',u.point =" +
                "    IF(g.reward = 'HIGH_RETURN', u.point, u.point + ROUND(u.point * 0.5, 0)),ucp.point = ucp.point - ROUND(g.point * 0.5, 0) " +
                "    where g.goal_state = 'ONGOING' and g.end_date < CURRENT_DATE and ucp.category = g.category and (select count(*) from certification where certification.state = 'ONGOING' and certification.goal_id = g.id)=0; ";
        jdbcTemplate.batchUpdate(sql);
    }
    public void updateTimeOutGoalCertRemain(){
        String sql = "update goal g set g.goal_state = 'WAITING_CERT_COMPLETE'" +
                "    where g.goal_state = 'ONGOING' and g.end_date < CURRENT_DATE  and (select count(*) from certification where certification.state = 'ONGOING' and certification.goal_id = g.id)=0";
        jdbcTemplate.batchUpdate(sql);
    }
    /**
     * 인증날에 인증을 올리지 않은 목표의 실패 횟수를 반영하는 쿼리
     */
    public void addFailCountAtNoCertAtCertDay(){
        String sql = "update many_time_goal mtg set fail_count = (select count(mtgcd.cert_date) from many_time_goal_cert_date mtgcd where mtgcd.goal_id = mtg.id and mtgcd.cert_date < CURRENT_DATE) - (select count(*) from certification c where c.goal_id = mtg.id and c.date<current_date) + (select count(*) from certification where certification.goal_id = mtg.id and certification.state='FAIL') where " +
                "    (select count(mtgcd.cert_date) from many_time_goal_cert_date mtgcd where mtgcd.goal_id = mtg.id and mtgcd.cert_date < CURRENT_DATE) != (select count(*) from certification c where c.goal_id = mtg.id and c.date<current_date)";
        jdbcTemplate.batchUpdate(sql);
    }

    /**
     * 실패 횟수가 70% 를 넘은 목표를 실패 처리하는 sql
     */
    public void updateManyTimeGoalToFail(){
        String sql = "update goal g join user u on g.user_id = u.id join user_category_point ucp on u.id = ucp.user_id join many_time_goal mtg on g.id = mtg.id" +
                " set g.goal_state = 'FAIL', u.point = IF(g.reward = 'HIGH_RETURN', u.point, u.point + ROUND(u.point * 0.5, 0))," +
                "    ucp.point = ucp.point - IF(g.reward = 'HIGH_RETURN', g.point,ROUND(g.point * 0.5, 0))\n" +
                "where mtg.fail_count >= ROUND(0.7 * (select count(*) from many_time_goal_cert_date mtgcd where mtgcd.goal_id = g.id)) and ucp.category = g.category and g.goal_state ='ONGOING'";
        jdbcTemplate.batchUpdate(sql);
    }
}

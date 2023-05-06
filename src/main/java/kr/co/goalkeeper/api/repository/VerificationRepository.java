package kr.co.goalkeeper.api.repository;

import kr.co.goalkeeper.api.model.entity.goal.Verification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VerificationRepository extends JpaRepository<Verification, Long> {
    boolean existsByCertification_IdAndUser_Id(long certId,long userId);
    List<Verification> findAllByCertification_IdInAndUser_Id(List<Long> certIds,long userId);
}

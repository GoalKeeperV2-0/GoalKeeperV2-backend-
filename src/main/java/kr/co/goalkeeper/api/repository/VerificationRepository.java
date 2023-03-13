package kr.co.goalkeeper.api.repository;

import kr.co.goalkeeper.api.model.entity.Verification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VerificationRepository extends JpaRepository<Verification, Long> {
    boolean existsByCertification_IdAndUser_Id(long certId,long userId);
}

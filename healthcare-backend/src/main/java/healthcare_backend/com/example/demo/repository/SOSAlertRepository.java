package healthcare_backend.com.example.demo.repository;

import healthcare_backend.com.example.demo.model.SOSAlert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SOSAlertRepository extends JpaRepository<SOSAlert, Long> {
    List<SOSAlert> findByUserEmailOrderByCreatedAtDesc(String userEmail);
    List<SOSAlert> findByStatusOrderByCreatedAtDesc(String status);
}

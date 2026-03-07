package healthcare_backend.com.example.demo.repository;

import healthcare_backend.com.example.demo.model.PatientTracking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PatientTrackingRepository extends JpaRepository<PatientTracking, Long> {
    Optional<PatientTracking> findByConsentToken(String consentToken);
    Optional<PatientTracking> findByAppointmentId(Long appointmentId);
    List<PatientTracking> findByDoctorIdAndConsentGiven(String doctorId, boolean consentGiven);
    boolean existsByAppointmentId(Long appointmentId);
}

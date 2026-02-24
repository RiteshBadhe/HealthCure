package healthcare_backend.com.example.demo.repository;

import healthcare_backend.com.example.demo.model.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    List<Appointment> findByDoctorId(String doctorId);
    List<Appointment> findByPatientEmail(String patientEmail);
    List<Appointment> findByStatus(String status);
}

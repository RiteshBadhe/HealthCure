package healthcare_backend.com.example.demo.repository;

import healthcare_backend.com.example.demo.model.DoctorAvailability;
import healthcare_backend.com.example.demo.model.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.DayOfWeek;
import java.util.List;

@Repository
public interface DoctorAvailabilityRepository extends JpaRepository<DoctorAvailability, Long> {
    List<DoctorAvailability> findByDoctorAndDayOfWeek(Doctor doctor, DayOfWeek dayOfWeek);
}

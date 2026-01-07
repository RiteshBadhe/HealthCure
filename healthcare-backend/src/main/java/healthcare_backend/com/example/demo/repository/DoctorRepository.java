package healthcare_backend.com.example.demo.repository;

import healthcare_backend.com.example.demo.model.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@RepositoryResources
public interface DoctorRepository extends JpaRepository<Doctor, Long> {

}

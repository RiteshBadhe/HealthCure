package healthcare_backend.com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import healthcare_backend.com.example.demo.model.Patient;

@RepositoryResources
public interface PatientRepository extends JpaRepository<Patient, Long>{
    
}

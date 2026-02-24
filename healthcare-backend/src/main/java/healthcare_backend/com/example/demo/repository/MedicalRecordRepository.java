package healthcare_backend.com.example.demo.repository;

import healthcare_backend.com.example.demo.model.MedicalRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MedicalRecordRepository extends JpaRepository<MedicalRecord, Long> {
    
    // Find all records for a specific patient
    List<MedicalRecord> findByPatientId(Long patientId);
    
    // Find all records for a specific doctor
    List<MedicalRecord> findByDoctorId(Long doctorId);
    
    // Find by record type
    List<MedicalRecord> findByRecordType(String recordType);
    
    // Find by patient and record type
    List<MedicalRecord> findByPatientIdAndRecordType(Long patientId, String recordType);
    
    // Find by patient ordered by creation date descending
    List<MedicalRecord> findByPatientIdOrderByCreatedAtDesc(Long patientId);
}

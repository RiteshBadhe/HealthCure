package healthcare_backend.com.example.demo.controller;

import healthcare_backend.com.example.demo.annotation.RequireRole;
import healthcare_backend.com.example.demo.model.MedicalRecord;
import healthcare_backend.com.example.demo.repository.MedicalRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/medical-records")
@CrossOrigin(origins = "http://localhost:3000")
public class MedicalRecordController {
    
    @Autowired
    private MedicalRecordRepository medicalRecordRepository;
    
    // Get all records for a patient
    @GetMapping("/patient/{patientId}")
    @RequireRole({"PATIENT", "DOCTOR", "ADMIN"})
    public ResponseEntity<List<MedicalRecord>> getPatientRecords(@PathVariable Long patientId) {
        List<MedicalRecord> records = medicalRecordRepository.findByPatientIdOrderByCreatedAtDesc(patientId);
        return ResponseEntity.ok(records);
    }
    
    // Get all records for a doctor
    @GetMapping("/doctor/{doctorId}")
    @RequireRole({"DOCTOR", "ADMIN"})
    public ResponseEntity<List<MedicalRecord>> getDoctorRecords(@PathVariable Long doctorId) {
        List<MedicalRecord> records = medicalRecordRepository.findByDoctorId(doctorId);
        return ResponseEntity.ok(records);
    }
    
    // Get a specific record by ID
    @GetMapping("/{id}")
    @RequireRole({"PATIENT", "DOCTOR", "ADMIN"})
    public ResponseEntity<MedicalRecord> getRecordById(@PathVariable Long id) {
        Optional<MedicalRecord> record = medicalRecordRepository.findById(id);
        return record.map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
    }
    
    // Create a new medical record
    @PostMapping
    @RequireRole({"DOCTOR", "ADMIN"})
    public ResponseEntity<MedicalRecord> createRecord(@RequestBody MedicalRecord record) {
        MedicalRecord savedRecord = medicalRecordRepository.save(record);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedRecord);
    }
    
    // Update a medical record
    @PutMapping("/{id}")
    @RequireRole({"DOCTOR", "ADMIN"})
    public ResponseEntity<MedicalRecord> updateRecord(@PathVariable Long id, @RequestBody MedicalRecord record) {
        if (!medicalRecordRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        record.setId(id);
        MedicalRecord updatedRecord = medicalRecordRepository.save(record);
        return ResponseEntity.ok(updatedRecord);
    }
    
    // Delete a medical record
    @DeleteMapping("/{id}")
    @RequireRole({"DOCTOR", "ADMIN"})
    public ResponseEntity<Void> deleteRecord(@PathVariable Long id) {
        if (!medicalRecordRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        medicalRecordRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
    
    // Get records by type for a patient
    @GetMapping("/patient/{patientId}/type/{recordType}")
    @RequireRole({"PATIENT", "DOCTOR", "ADMIN"})
    public ResponseEntity<List<MedicalRecord>> getRecordsByType(
            @PathVariable Long patientId, 
            @PathVariable String recordType) {
        List<MedicalRecord> records = medicalRecordRepository.findByPatientIdAndRecordType(patientId, recordType);
        return ResponseEntity.ok(records);
    }
}

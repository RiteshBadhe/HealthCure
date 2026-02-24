package healthcare_backend.com.example.demo.controller;

import healthcare_backend.com.example.demo.annotation.RequireRole;
import healthcare_backend.com.example.demo.model.Patient;
import healthcare_backend.com.example.demo.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/patients")
@CrossOrigin(origins = "http://localhost:3000")
public class PatientController {

    @Autowired
    private PatientRepository patientRepository;

    @PostMapping
    @RequireRole({"PATIENT", "ADMIN"})
    public ResponseEntity<Patient> createPatient(@RequestBody Patient patient) {
        try {
            Patient savedPatient = patientRepository.save(patient);
            return new ResponseEntity<>(savedPatient, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping
    @RequireRole({"DOCTOR", "ADMIN"})
    public ResponseEntity<List<Patient>> getAllPatients() {
        try {
            List<Patient> patients = patientRepository.findAll();
            if (patients.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(patients, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    @RequireRole({"PATIENT", "DOCTOR", "ADMIN"})
    public ResponseEntity<Patient> getPatientById(@PathVariable Long id) {
        Optional<Patient> patientData = patientRepository.findById(id);

        return patientData
                .map(patient -> new ResponseEntity<>(patient, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PutMapping("/{id}")
    @RequireRole({"PATIENT", "DOCTOR", "ADMIN"})
    public ResponseEntity<Patient> updatePatient(@PathVariable Long id, @RequestBody Patient patient) {
        Optional<Patient> patientData = patientRepository.findById(id);

        if (patientData.isPresent()) {
            Patient existingPatient = patientData.get();
            existingPatient.setName(patient.getName());
            existingPatient.setAddress(patient.getAddress());
            existingPatient.setDisease(patient.getDisease());

            Patient updatedPatient = patientRepository.save(existingPatient);
            return new ResponseEntity<>(updatedPatient, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    @RequireRole({"ADMIN"})
    public ResponseEntity<Void> deletePatient(@PathVariable Long id) {
        try {
            if (!patientRepository.existsById(id)) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            patientRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping
    @RequireRole({"ADMIN"})
    public ResponseEntity<Void> deleteAllPatients() {
        try {
            patientRepository.deleteAll();
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

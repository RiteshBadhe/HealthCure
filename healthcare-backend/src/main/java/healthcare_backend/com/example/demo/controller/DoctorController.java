package healthcare_backend.com.example.demo.controller;

import healthcare_backend.com.example.demo.annotation.RequireRole;
import healthcare_backend.com.example.demo.model.Doctor;
import healthcare_backend.com.example.demo.repository.DoctorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/doctors")
@CrossOrigin(origins = "http://localhost:3000")
public class DoctorController {

    @Autowired
    private DoctorRepository doctorRepository;

    @GetMapping
    @RequireRole({"PATIENT", "DOCTOR", "ADMIN"})
    public List<Doctor> getAllDoctors() {
        return doctorRepository.findAll();
    }

    @GetMapping("/{id}")
    @RequireRole({"PATIENT", "DOCTOR", "ADMIN"})
    public ResponseEntity<Doctor> getDoctorById(@PathVariable Long id) {
        Optional<Doctor> doctor = doctorRepository.findById(id);
        return doctor.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @RequireRole({"ADMIN"})
    public Doctor addDoctor(@RequestBody Doctor doctor) {
        return doctorRepository.save(doctor);
    }

    @PutMapping("/{id}")
    @RequireRole({"DOCTOR", "ADMIN"})
    public Doctor updateDoctor(@PathVariable Long id, @RequestBody Doctor doctor) {
        Doctor existingDoctor = doctorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Doctor not found with id: " + id));

        existingDoctor.setPhoto(doctor.getPhoto());
        existingDoctor.setName(doctor.getName());
        existingDoctor.setSpecialization(doctor.getSpecialization());
        existingDoctor.setEmail(doctor.getEmail());
        existingDoctor.setPhone(doctor.getPhone());

        return doctorRepository.save(existingDoctor);
    }

    @DeleteMapping("/{id}")
    @RequireRole({"ADMIN"})
    public void deleteDoctor(@PathVariable Long id) {
        if (!doctorRepository.existsById(id)) {
            throw new RuntimeException("Doctor not found with id: " + id);
        }
        doctorRepository.deleteById(id);
    }
}

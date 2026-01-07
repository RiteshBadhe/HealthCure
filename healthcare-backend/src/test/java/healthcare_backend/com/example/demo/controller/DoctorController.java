package healthcare_backend.com.example.demo.controller;

import healthcare_backend.com.example.demo.model.Doctor;
import healthcare_backend.com.example.demo.repository.DoctorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/doctors")
@CrossOrigin(origins = "http://localhost:3000") // allow frontend access
public class DoctorController {

    @Autowired
    private DoctorRepository doctorRepository;

    @GetMapping
    public List<Doctor> getAllDoctors() {
        return doctorRepository.findAll();
    }

    @PostMapping
    public Doctor addDoctor(@RequestBody Doctor doctor) {
        return doctorRepository.save(doctor);
    }
    @PutMapping("/{id}")
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
public void deleteDoctor(@PathVariable Long id) {

    if (!doctorRepository.existsById(id)) {
        throw new RuntimeException("Doctor not found with id: " + id);
    }

    doctorRepository.deleteById(id);
}

}

package healthcare_backend.com.example.demo.controller;

import healthcare_backend.com.example.demo.model.Doctor;
import healthcare_backend.com.example.demo.model.User;
import healthcare_backend.com.example.demo.repository.DoctorRepository;
import healthcare_backend.com.example.demo.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private DoctorRepository doctorRepository;

    @PostMapping("/register")
    @Transactional
    public ResponseEntity<?> registerUser(@RequestBody User user) {
        try {
            Optional<User> existingUser = userRepository.findByEmail(user.getEmail());
            if (existingUser.isPresent()) {
                return ResponseEntity.badRequest()
                        .body(Map.of("message", "Email already exists"));
            }
            
            // Save user first
            User savedUser = userRepository.save(user);
            
            // If user is DOCTOR, automatically create Doctor profile
            if ("DOCTOR".equals(user.getRole())) {
                Doctor doctor = new Doctor();
                doctor.setName(user.getName());
                doctor.setEmail(user.getEmail());
                doctor.setSpecialization("General Medicine"); // Default, can be updated later
                doctor.setPhone(""); // Can be updated later
                doctor.setPhoto(""); // Can be updated later
                doctorRepository.save(doctor);
                
                System.out.println("✅ Created Doctor profile for: " + user.getEmail());
                
                return ResponseEntity.ok(Map.of(
                    "message", "Doctor account created successfully! Please update your profile.",
                    "userId", savedUser.getId(),
                    "requiresProfileSetup", true
                ));
            }
            
            return ResponseEntity.ok(Map.of("message", "User registered successfully"));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(Map.of("message", "Error: " + e.getMessage()));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody Map<String, String> request) {
        try {
            String email = request.get("email");
            String password = request.get("password");
            
            Optional<User> userOpt = userRepository.findByEmail(email);
            if (userOpt.isPresent() && userOpt.get().getPassword().equals(password)) {
                User user = userOpt.get();
                String token = "Bearer-" + email + "-" + System.currentTimeMillis();
                
                Map<String, Object> response = new HashMap<>();
                response.put("token", token);
                response.put("role", user.getRole());
                response.put("userId", user.getId());
                response.put("email", user.getEmail());
                response.put("message", "Login successful");
                
                // If user is a DOCTOR, also fetch and return their Doctor ID
                if ("DOCTOR".equals(user.getRole())) {
                    Optional<Doctor> doctorOpt = doctorRepository.findByEmail(email);
                    if (doctorOpt.isPresent()) {
                        response.put("doctorId", doctorOpt.get().getId());
                        System.out.println("✅ Doctor login - doctorId: " + doctorOpt.get().getId());
                    } else {
                        System.out.println("⚠️ DOCTOR user logged in but no Doctor record found for email: " + email);
                    }
                }
                
                return ResponseEntity.ok(response);
            }
            return ResponseEntity.badRequest().body(Map.of("message", "Invalid credentials"));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(Map.of("message", "Login failed"));
        }
    }
}

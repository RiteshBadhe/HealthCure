package healthcare_backend.com.example.demo.controller;

import healthcare_backend.com.example.demo.model.SOSAlert;
import healthcare_backend.com.example.demo.model.Notification;
import healthcare_backend.com.example.demo.model.User;
import healthcare_backend.com.example.demo.repository.SOSAlertRepository;
import healthcare_backend.com.example.demo.repository.NotificationRepository;
import healthcare_backend.com.example.demo.repository.UserRepository;
import healthcare_backend.com.example.demo.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class SOSController {

    @Autowired
    private SOSAlertRepository sosAlertRepository;

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailService emailService;

    @PostMapping("/sos/send")
    public ResponseEntity<?> sendSOS(@RequestBody Map<String, Object> request) {
        try {
            String userEmail = (String) request.get("userEmail");
            String userName = (String) request.get("userName");
            Double latitude = Double.parseDouble(request.get("latitude").toString());
            Double longitude = Double.parseDouble(request.get("longitude").toString());
            String emergencyContactEmail = (String) request.get("emergencyContactEmail");

            // Create SOS Alert record
            SOSAlert sosAlert = new SOSAlert();
            sosAlert.setUserEmail(userEmail);
            sosAlert.setUserName(userName);
            sosAlert.setLatitude(latitude);
            sosAlert.setLongitude(longitude);
            sosAlert.setEmergencyContactEmail(emergencyContactEmail);
            
            // Generate Google Maps link
            String googleMapsLink = String.format("https://www.google.com/maps?q=%.6f,%.6f", latitude, longitude);
            sosAlert.setLocationAddress(googleMapsLink);
            
            SOSAlert savedAlert = sosAlertRepository.save(sosAlert);

            // Find user by email to get userId
            User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + userEmail));

            // Create notification for user
            Notification notification = new Notification();
            notification.setUserId(user.getId());
            notification.setUserEmail(userEmail);
            notification.setTitle("🚨 SOS Alert Sent");
            notification.setMessage(String.format("Emergency alert sent successfully. Location: %.6f, %.6f", latitude, longitude));
            notification.setType("EMERGENCY");
            notification.setPriority("HIGH");
            notificationRepository.save(notification);

            // Send email to emergency contact if provided
            if (emergencyContactEmail != null && !emergencyContactEmail.isEmpty()) {
                emailService.sendSOSEmail(emergencyContactEmail, userName, latitude, longitude, googleMapsLink);
            }

            // Also send to user's own email
            emailService.sendSOSEmail(userEmail, userName, latitude, longitude, googleMapsLink);

            return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "SOS alert sent successfully!",
                "alertId", savedAlert.getId(),
                "googleMapsLink", googleMapsLink
            ));

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", "Failed to send SOS: " + e.getMessage()
            ));
        }
    }

    @GetMapping("/sos/alerts/{email}")
    public ResponseEntity<List<SOSAlert>> getUserSOSAlerts(@PathVariable String email) {
        List<SOSAlert> alerts = sosAlertRepository.findByUserEmailOrderByCreatedAtDesc(email);
        return ResponseEntity.ok(alerts);
    }

    @GetMapping("/sos/alerts")
    public ResponseEntity<List<SOSAlert>> getAllSOSAlerts() {
        List<SOSAlert> alerts = sosAlertRepository.findAll();
        return ResponseEntity.ok(alerts);
    }

    @PutMapping("/sos/alerts/{id}/status")
    public ResponseEntity<?> updateSOSStatus(@PathVariable Long id, @RequestBody Map<String, String> request) {
        try {
            SOSAlert alert = sosAlertRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("SOS Alert not found"));
            
            alert.setStatus(request.get("status"));
            sosAlertRepository.save(alert);

            return ResponseEntity.ok(Map.of("success", true, "message", "Status updated"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", e.getMessage()));
        }
    }
}

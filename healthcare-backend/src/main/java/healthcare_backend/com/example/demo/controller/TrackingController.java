package healthcare_backend.com.example.demo.controller;

import healthcare_backend.com.example.demo.model.PatientTracking;
import healthcare_backend.com.example.demo.repository.AppointmentRepository;
import healthcare_backend.com.example.demo.repository.PatientTrackingRepository;
import healthcare_backend.com.example.demo.scheduler.AppointmentTrackingScheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/tracking")
@CrossOrigin(origins = "http://localhost:3000")
public class TrackingController {

    @Autowired
    private PatientTrackingRepository trackingRepository;

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private AppointmentTrackingScheduler trackingScheduler;

    // ✅ MANUAL TRIGGER — Send tracking consent email for any appointment ID (for testing)
    @PostMapping("/trigger/{appointmentId}")
    public ResponseEntity<Map<String, Object>> manualTrigger(@PathVariable Long appointmentId) {
        Map<String, Object> response = new HashMap<>();
        return appointmentRepository.findById(appointmentId).map(appt -> {
            boolean sent = trackingScheduler.sendConsentEmailForAppointment(appt);
            response.put("success", sent);
            response.put("message", sent
                ? "Consent email sent to " + appt.getPatientEmail()
                : "Tracking email was already sent for this appointment.");
            return ResponseEntity.ok(response);
        }).orElseGet(() -> {
            response.put("success", false);
            response.put("message", "Appointment #" + appointmentId + " not found.");
            return ResponseEntity.badRequest().body(response);
        });
    }

    // ✅ Patient clicks "Allow Tracking" link in email → sets consent
    @GetMapping("/consent/{token}")
    public ResponseEntity<Map<String, Object>> giveConsent(@PathVariable String token) {
        Map<String, Object> response = new HashMap<>();
        Optional<PatientTracking> opt = trackingRepository.findByConsentToken(token);

        if (opt.isEmpty()) {
            response.put("success", false);
            response.put("message", "Invalid or expired tracking link.");
            return ResponseEntity.badRequest().body(response);
        }

        PatientTracking tracking = opt.get();
        if (tracking.getExpiresAt() != null && LocalDateTime.now().isAfter(tracking.getExpiresAt())) {
            response.put("success", false);
            response.put("message", "This tracking link has expired.");
            return ResponseEntity.badRequest().body(response);
        }

        tracking.setConsentGiven(true);
        trackingRepository.save(tracking);

        response.put("success", true);
        response.put("token", token);
        response.put("patientName", tracking.getPatientName());
        response.put("doctorName", tracking.getDoctorName());
        response.put("appointmentId", tracking.getAppointmentId());
        response.put("message", "Consent granted. Location tracking will now begin.");
        return ResponseEntity.ok(response);
    }

    // ✅ Patient's browser sends live location updates
    @PostMapping("/location")
    public ResponseEntity<Map<String, Object>> updateLocation(@RequestBody Map<String, Object> request) {
        Map<String, Object> response = new HashMap<>();
        String token = (String) request.get("token");

        Optional<PatientTracking> opt = trackingRepository.findByConsentToken(token);
        if (opt.isEmpty()) {
            response.put("success", false);
            return ResponseEntity.badRequest().body(response);
        }

        PatientTracking tracking = opt.get();
        if (!tracking.isConsentGiven()) {
            response.put("success", false);
            response.put("message", "No consent given yet.");
            return ResponseEntity.badRequest().body(response);
        }

        tracking.setLatitude(Double.parseDouble(request.get("latitude").toString()));
        tracking.setLongitude(Double.parseDouble(request.get("longitude").toString()));
        tracking.setLastLocationUpdate(LocalDateTime.now());
        trackingRepository.save(tracking);

        response.put("success", true);
        return ResponseEntity.ok(response);
    }

    // ✅ Doctor polls this to get latest patient location for an appointment
    @GetMapping("/location/{appointmentId}")
    public ResponseEntity<Map<String, Object>> getPatientLocation(@PathVariable Long appointmentId) {
        Map<String, Object> response = new HashMap<>();
        Optional<PatientTracking> opt = trackingRepository.findByAppointmentId(appointmentId);

        if (opt.isEmpty()) {
            response.put("success", false);
            response.put("message", "No tracking record found.");
            return ResponseEntity.ok(response);
        }

        PatientTracking tracking = opt.get();
        response.put("success", true);
        response.put("consentGiven", tracking.isConsentGiven());
        response.put("patientName", tracking.getPatientName());
        response.put("latitude", tracking.getLatitude());
        response.put("longitude", tracking.getLongitude());
        response.put("lastUpdate", tracking.getLastLocationUpdate());
        return ResponseEntity.ok(response);
    }

    // ✅ Doctor gets all tracked patients for their ID
    @GetMapping("/doctor/{doctorId}")
    public ResponseEntity<List<PatientTracking>> getDoctorTrackedPatients(@PathVariable String doctorId) {
        List<PatientTracking> list = trackingRepository.findByDoctorIdAndConsentGiven(doctorId, true);
        return ResponseEntity.ok(list);
    }
}

package healthcare_backend.com.example.demo.controller;

import healthcare_backend.com.example.demo.model.Appointment;
import healthcare_backend.com.example.demo.model.Notification;
import healthcare_backend.com.example.demo.repository.AppointmentRepository;
import healthcare_backend.com.example.demo.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:3000")
public class AppointmentController {

    @Autowired
    private AppointmentRepository appointmentRepository;
    
    @Autowired
    private NotificationRepository notificationRepository;

    // ✅ CREATE APPOINTMENT
@PostMapping("/appointments")
    public ResponseEntity<Map<String, Object>> createAppointment(@RequestBody Map<String, String> request) {

        Map<String, Object> response = new HashMap<>();

        try {
            Appointment appointment = new Appointment();

            appointment.setPatientName(request.get("patientName"));
            appointment.setPatientEmail(request.get("patientEmail"));
            appointment.setPatientPhone(request.get("patientPhone"));
            appointment.setAppointmentDate(request.get("appointmentDate"));
            appointment.setAppointmentTime(request.get("appointmentTime"));
            appointment.setReason(request.get("reason") != null ? request.get("reason") : "");
            appointment.setDoctorId(request.get("doctorId"));
            appointment.setDoctorName(request.get("doctorName"));
            appointment.setStatus("pending");

            Appointment saved = appointmentRepository.save(appointment);
            
            // 🔔 CREATE NOTIFICATION FOR PATIENT
            try {
                Notification patientNotification = new Notification();
                patientNotification.setUserId(1L); // You should get actual userId from request
                patientNotification.setUserEmail(appointment.getPatientEmail());
                patientNotification.setTitle("Appointment Booked");
                patientNotification.setMessage(
                    "Your appointment with " + appointment.getDoctorName() + 
                    " has been booked for " + appointment.getAppointmentDate() + 
                    " at " + appointment.getAppointmentTime()
                );
                patientNotification.setType("APPOINTMENT");
                patientNotification.setPriority("HIGH");
                patientNotification.setIsRead(false);
                notificationRepository.save(patientNotification);
            } catch (Exception e) {
                System.err.println("Failed to create notification: " + e.getMessage());
            }

            response.put("success", true);
            response.put("message", "Appointment booked successfully!");
            response.put("appointmentId", saved.getId());

            return ResponseEntity.ok(response);

        } catch (Exception e) {

            response.put("success", false);
            response.put("message", e.getMessage());

            return ResponseEntity.badRequest().body(response);
        }
    }

    // ✅ GET APPOINTMENTS FOR DOCTOR
    @GetMapping("/appointments/doctor/{doctorId}")
    public ResponseEntity<List<Appointment>> getDoctorAppointments(@PathVariable String doctorId) {
        List<Appointment> appointments = appointmentRepository.findByDoctorId(doctorId);
        return ResponseEntity.ok(appointments);
    }

    // ✅ GET APPOINTMENTS FOR PATIENT
    @GetMapping("/appointments/patient/{email}")
    public ResponseEntity<List<Appointment>> getPatientAppointments(@PathVariable String email) {
        List<Appointment> appointments = appointmentRepository.findByPatientEmail(email);
        return ResponseEntity.ok(appointments);
    }
    
    // ✅ GET ALL APPOINTMENTS (FOR CALENDAR VIEW - ADMIN)
    @GetMapping("/appointments")
    public ResponseEntity<List<Appointment>> getAllAppointments() {
        List<Appointment> appointments = appointmentRepository.findAll();
        return ResponseEntity.ok(appointments);
    }
    
    // ✅ UPDATE APPOINTMENT STATUS
    @PutMapping("/appointments/{id}/status")
    public ResponseEntity<Map<String, Object>> updateAppointmentStatus(
            @PathVariable Long id, 
            @RequestBody Map<String, String> request) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Appointment not found"));
            
            String newStatus = request.get("status");
            appointment.setStatus(newStatus);
            appointmentRepository.save(appointment);
            
            // 🔔 CREATE NOTIFICATION FOR STATUS CHANGE
            try {
                Notification notification = new Notification();
                notification.setUserId(1L);
                notification.setUserEmail(appointment.getPatientEmail());
                notification.setTitle("Appointment Status Updated");
                notification.setMessage(
                    "Your appointment with " + appointment.getDoctorName() + 
                    " on " + appointment.getAppointmentDate() + 
                    " is now " + newStatus.toUpperCase()
                );
                notification.setType("APPOINTMENT");
                notification.setPriority("MEDIUM");
                notification.setIsRead(false);
                notificationRepository.save(notification);
            } catch (Exception e) {
                System.err.println("Failed to create notification: " + e.getMessage());
            }
            
            response.put("success", true);
            response.put("message", "Appointment status updated");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    // ✅ CANCEL/DELETE APPOINTMENT
    @DeleteMapping("/appointments/{id}")
    public ResponseEntity<Map<String, Object>> deleteAppointment(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            Appointment appointment = appointmentRepository.findById(id).orElse(null);
            appointmentRepository.deleteById(id);
            
            // 🔔 CREATE NOTIFICATION FOR CANCELLATION
            if (appointment != null) {
                try {
                    Notification notification = new Notification();
                    notification.setUserId(1L);
                    notification.setUserEmail(appointment.getPatientEmail());
                    notification.setTitle("Appointment Cancelled");
                    notification.setMessage(
                        "Your appointment with " + appointment.getDoctorName() + 
                        " on " + appointment.getAppointmentDate() + 
                        " has been cancelled."
                    );
                    notification.setType("APPOINTMENT");
                    notification.setPriority("HIGH");
                    notification.setIsRead(false);
                    notificationRepository.save(notification);
                } catch (Exception e) {
                    System.err.println("Failed to create notification: " + e.getMessage());
                }
            }
            
            response.put("success", true);
            response.put("message", "Appointment cancelled");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    // ✅ RESCHEDULE APPOINTMENT
    @PutMapping("/appointments/{id}/reschedule")
    public ResponseEntity<Map<String, Object>> rescheduleAppointment(
            @PathVariable Long id,
            @RequestBody Map<String, String> request) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Appointment not found"));
            
            appointment.setAppointmentDate(request.get("appointmentDate"));
            appointment.setAppointmentTime(request.get("appointmentTime"));
            appointmentRepository.save(appointment);
            
            // 🔔 CREATE NOTIFICATION FOR RESCHEDULING
            try {
                Notification notification = new Notification();
                notification.setUserId(1L);
                notification.setUserEmail(appointment.getPatientEmail());
                notification.setTitle("Appointment Rescheduled");
                notification.setMessage(
                    "Your appointment with " + appointment.getDoctorName() + 
                    " has been rescheduled to " + request.get("appointmentDate") + 
                    " at " + request.get("appointmentTime")
                );
                notification.setType("APPOINTMENT");
                notification.setPriority("HIGH");
                notification.setIsRead(false);
                notificationRepository.save(notification);
            } catch (Exception e) {
                System.err.println("Failed to create notification: " + e.getMessage());
            }
            
            response.put("success", true);
            response.put("message", "Appointment rescheduled successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
}

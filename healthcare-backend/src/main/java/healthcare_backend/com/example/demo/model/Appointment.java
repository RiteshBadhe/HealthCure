package healthcare_backend.com.example.demo.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "appointments")
public class Appointment {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "Patient name required")
    @Size(min = 2, max = 100)
    @Column(name = "patient_name", nullable = false)
    private String patientName;
    
    @NotBlank(message = "Email required")
    @Email(message = "Valid email required")
    @Column(name = "patient_email", nullable = false)
    private String patientEmail;
    
    @NotBlank(message = "Phone required")
    @Size(min = 10, max = 15)
    @Column(name = "patient_phone", nullable = false)
    private String patientPhone;
    
    @NotBlank(message = "Date required")
    @Column(name = "appointment_date", nullable = false)
    private String appointmentDate;
    
    @NotBlank(message = "Time required")
    @Column(name = "appointment_time", nullable = false)
    private String appointmentTime;
    
    @Column(name = "reason", columnDefinition = "TEXT")
    private String reason;
    
    @NotBlank(message = "Doctor ID required")
    @Column(name = "doctor_id", nullable = false)
    private String doctorId;
    
    @NotBlank(message = "Doctor name required")
    @Column(name = "doctor_name", nullable = false)
    private String doctorName;
    
    @Column(name = "status", nullable = false, length = 20)
    private String status = "pending";
    
    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    // ✅ DEFAULT CONSTRUCTOR (REQUIRED)
    public Appointment() {}

    // ✅ ALL GETTERS
    public Long getId() { return id; }
    public String getPatientName() { return patientName; }
    public String getPatientEmail() { return patientEmail; }
    public String getPatientPhone() { return patientPhone; }
    public String getAppointmentDate() { return appointmentDate; }
    public String getAppointmentTime() { return appointmentTime; }
    public String getReason() { return reason; }
    public String getDoctorId() { return doctorId; }
    public String getDoctorName() { return doctorName; }
    public String getStatus() { return status; }
    public LocalDateTime getCreatedAt() { return createdAt; }

    // ✅ ALL SETTERS
    public void setId(Long id) { this.id = id; }
    public void setPatientName(String patientName) { this.patientName = patientName; }
    public void setPatientEmail(String patientEmail) { this.patientEmail = patientEmail; }
    public void setPatientPhone(String patientPhone) { this.patientPhone = patientPhone; }
    public void setAppointmentDate(String appointmentDate) { this.appointmentDate = appointmentDate; }
    public void setAppointmentTime(String appointmentTime) { this.appointmentTime = appointmentTime; }
    public void setReason(String reason) { this.reason = reason; }
    public void setDoctorId(String doctorId) { this.doctorId = doctorId; }
    public void setDoctorName(String doctorName) { this.doctorName = doctorName; }
    public void setStatus(String status) { this.status = status; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}

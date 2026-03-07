package healthcare_backend.com.example.demo.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "patient_tracking")
public class PatientTracking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "appointment_id", nullable = false)
    private Long appointmentId;

    @Column(name = "patient_email", nullable = false)
    private String patientEmail;

    @Column(name = "patient_name")
    private String patientName;

    @Column(name = "doctor_id")
    private String doctorId;

    @Column(name = "doctor_name")
    private String doctorName;

    // Unique token sent in the consent email link
    @Column(name = "consent_token", unique = true, nullable = false)
    private String consentToken;

    // Has the patient clicked "Allow Tracking"?
    @Column(name = "consent_given", nullable = false)
    private boolean consentGiven = false;

    // Live location
    @Column(name = "latitude")
    private Double latitude;

    @Column(name = "longitude")
    private Double longitude;

    @Column(name = "last_location_update")
    private LocalDateTime lastLocationUpdate;

    // When was the consent email sent
    @Column(name = "email_sent_at")
    private LocalDateTime emailSentAt;

    // Token expires after the appointment date
    @Column(name = "expires_at")
    private LocalDateTime expiresAt;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    // Constructors
    public PatientTracking() {}

    // Getters & Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getAppointmentId() { return appointmentId; }
    public void setAppointmentId(Long appointmentId) { this.appointmentId = appointmentId; }

    public String getPatientEmail() { return patientEmail; }
    public void setPatientEmail(String patientEmail) { this.patientEmail = patientEmail; }

    public String getPatientName() { return patientName; }
    public void setPatientName(String patientName) { this.patientName = patientName; }

    public String getDoctorId() { return doctorId; }
    public void setDoctorId(String doctorId) { this.doctorId = doctorId; }

    public String getDoctorName() { return doctorName; }
    public void setDoctorName(String doctorName) { this.doctorName = doctorName; }

    public String getConsentToken() { return consentToken; }
    public void setConsentToken(String consentToken) { this.consentToken = consentToken; }

    public boolean isConsentGiven() { return consentGiven; }
    public void setConsentGiven(boolean consentGiven) { this.consentGiven = consentGiven; }

    public Double getLatitude() { return latitude; }
    public void setLatitude(Double latitude) { this.latitude = latitude; }

    public Double getLongitude() { return longitude; }
    public void setLongitude(Double longitude) { this.longitude = longitude; }

    public LocalDateTime getLastLocationUpdate() { return lastLocationUpdate; }
    public void setLastLocationUpdate(LocalDateTime lastLocationUpdate) { this.lastLocationUpdate = lastLocationUpdate; }

    public LocalDateTime getEmailSentAt() { return emailSentAt; }
    public void setEmailSentAt(LocalDateTime emailSentAt) { this.emailSentAt = emailSentAt; }

    public LocalDateTime getExpiresAt() { return expiresAt; }
    public void setExpiresAt(LocalDateTime expiresAt) { this.expiresAt = expiresAt; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}

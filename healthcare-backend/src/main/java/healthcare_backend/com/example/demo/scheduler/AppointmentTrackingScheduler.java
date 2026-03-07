package healthcare_backend.com.example.demo.scheduler;

import healthcare_backend.com.example.demo.model.Appointment;
import healthcare_backend.com.example.demo.model.PatientTracking;
import healthcare_backend.com.example.demo.repository.AppointmentRepository;
import healthcare_backend.com.example.demo.repository.PatientTrackingRepository;
import healthcare_backend.com.example.demo.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.UUID;

@Component
public class AppointmentTrackingScheduler {

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private PatientTrackingRepository trackingRepository;

    @Autowired
    private EmailService emailService;

    // Runs every 60 seconds
    @Scheduled(fixedDelay = 60000)
    public void checkUpcomingAppointmentsAndSendConsentEmail() {
        System.out.println("🔍 [Scheduler] Checking for appointments in ~30 minutes...");
        List<Appointment> all = appointmentRepository.findByStatus("pending");
        LocalDateTime now = LocalDateTime.now();
        // Wide 20-minute window (25–45 min before appointment) so the scheduler never misses it
        LocalDateTime windowStart = now.plusMinutes(25);
        LocalDateTime windowEnd   = now.plusMinutes(45);

        // Handles both "H:mm" (4:20) and "HH:mm" (04:20)
        DateTimeFormatter timeFormatter = new DateTimeFormatterBuilder()
                .appendOptional(DateTimeFormatter.ofPattern("HH:mm"))
                .appendOptional(DateTimeFormatter.ofPattern("H:mm"))
                .toFormatter();

        for (Appointment appt : all) {
            try {
                LocalDate date = LocalDate.parse(appt.getAppointmentDate().trim(), DateTimeFormatter.ISO_LOCAL_DATE);
                LocalTime time = LocalTime.parse(appt.getAppointmentTime().trim(), timeFormatter);
                LocalDateTime apptDateTime = LocalDateTime.of(date, time);
                // !isBefore handles exact boundary (e.g. appointment is exactly 25 min away)
                if (!apptDateTime.isBefore(windowStart) && !apptDateTime.isAfter(windowEnd)) {
                    sendConsentEmailForAppointment(appt);
                }
            } catch (DateTimeParseException e) {
                System.err.println("⚠️  Bad date/time on appointment #" + appt.getId()
                        + " → '" + appt.getAppointmentDate() + "' '" + appt.getAppointmentTime() + "'");
            }
        }
    }

    // Public so TrackingController can call it for manual/test triggers
    public boolean sendConsentEmailForAppointment(Appointment appt) {
        if (trackingRepository.existsByAppointmentId(appt.getId())) {
            System.out.println("⏭️  Tracking already set up for appointment #" + appt.getId());
            return false;
        }

        String token = UUID.randomUUID().toString();
        PatientTracking tracking = new PatientTracking();
        tracking.setAppointmentId(appt.getId());
        tracking.setPatientEmail(appt.getPatientEmail());
        tracking.setPatientName(appt.getPatientName());
        tracking.setDoctorId(appt.getDoctorId());
        tracking.setDoctorName(appt.getDoctorName());
        tracking.setConsentToken(token);
        tracking.setConsentGiven(false);
        tracking.setEmailSentAt(LocalDateTime.now());
        tracking.setExpiresAt(LocalDateTime.now().plusHours(4));
        trackingRepository.save(tracking);

        String consentLink = "http://localhost:3000/tracking/consent/" + token;
        String subject = "📍 Allow Location Tracking for Your Upcoming Appointment";
        String body = "Dear " + appt.getPatientName() + ",\n\n"
                + "Your appointment with " + appt.getDoctorName()
                + " is coming up soon (" + appt.getAppointmentDate() + " at " + appt.getAppointmentTime() + ").\n\n"
                + "To help the clinic track your arrival and reduce wait time, "
                + "please click the link below to share your live location with your doctor:\n\n"
                + "   " + consentLink + "\n\n"
                + "Your doctor will see your location on a map in real-time once you allow it.\n\n"
                + "⚠️ This link expires in 4 hours. Location is only shared with your assigned doctor.\n\n"
                + "If you do NOT wish to be tracked, simply ignore this email.\n\n"
                + "— HealthCare Team";

        emailService.sendEmail(appt.getPatientEmail(), subject, body);
        System.out.println("✅ [Tracking] Email sent to: " + appt.getPatientEmail()
                + " | appointment #" + appt.getId() + " | token: " + token);
        return true;
    }
}

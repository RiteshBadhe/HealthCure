package healthcare_backend.com.example.demo.service;

import healthcare_backend.com.example.demo.model.Appointment;
import healthcare_backend.com.example.demo.model.Doctor;
import healthcare_backend.com.example.demo.repository.AppointmentRepository;
import healthcare_backend.com.example.demo.repository.DoctorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AppointmentService {

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private EmailService emailService;

    public void checkDoctorAvailabilityAndNotifyPatients(Long doctorId) {
        Doctor doctor = doctorRepository.findById(doctorId).orElse(null);
        if (doctor == null || !doctor.isAvailable()) return;

        List<Appointment> appointments = appointmentRepository.findByDoctorIdOrderByPriorityDesc(doctorId.toString());

        for (Appointment appointment : appointments) {
            boolean isPending = "pending".equals(appointment.getStatus());
            boolean isEmergency = appointment.getPriority() != null && appointment.getPriority() >= 1;

            if (isPending && isEmergency) {
                String subject = "🚨 URGENT: Dr. " + doctor.getName() + " is now available — Come now!";
                String body = "Dear " + appointment.getPatientName() + ",\n\n"
                    + "Great news! Dr. " + doctor.getName() + " (" + doctor.getSpecialization() + ") "
                    + "is currently FREE and available to see you RIGHT NOW.\n\n"
                    + "📅 Your appointment was scheduled for: " + appointment.getAppointmentDate()
                    + " at " + appointment.getAppointmentTime() + "\n"
                    + "📋 Reason: " + (appointment.getReason() != null && !appointment.getReason().isEmpty() ? appointment.getReason() : "Not specified") + "\n\n"
                    + "Since your case was marked as EMERGENCY / HIGH PRIORITY, "
                    + "please head to the clinic immediately or contact us to confirm.\n\n"
                    + "📞 Doctor's Contact: " + (doctor.getPhone() != null ? doctor.getPhone() : "N/A") + "\n\n"
                    + "Please respond quickly as this slot may not remain available for long.\n\n"
                    + "— HealthCare Team";

                emailService.sendEmail(appointment.getPatientEmail(), subject, body);
                System.out.println("✅ Emergency notification sent to: " + appointment.getPatientEmail());
            }
        }
    }
}

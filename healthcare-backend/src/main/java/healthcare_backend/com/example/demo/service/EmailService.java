package healthcare_backend.com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendSOSEmail(String toEmail, String userName, double latitude, double longitude, String googleMapsLink) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("healthcare.sos@example.com");
            message.setTo(toEmail);
            message.setSubject("🚨 EMERGENCY SOS ALERT - " + userName);
            
            String emailBody = String.format(
                "⚠️ EMERGENCY ALERT ⚠️\n\n" +
                "An SOS alert has been triggered by: %s\n\n" +
                "📍 Location Details:\n" +
                "Latitude: %.6f\n" +
                "Longitude: %.6f\n\n" +
                "🗺️ View on Google Maps:\n%s\n\n" +
                "⏰ Time: %s\n\n" +
                "Please respond immediately!\n\n" +
                "— HealthCare Emergency System",
                userName,
                latitude,
                longitude,
                googleMapsLink,
                java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("dd MMM yyyy, hh:mm a"))
            );
            
            message.setText(emailBody);
            mailSender.send(message);
            
            System.out.println("✅ SOS Email sent successfully to: " + toEmail);
        } catch (Exception e) {
            System.err.println("❌ Failed to send SOS email: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void sendNotificationEmail(String toEmail, String subject, String body) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("healthcare.notifications@example.com");
            message.setTo(toEmail);
            message.setSubject(subject);
            message.setText(body);
            mailSender.send(message);
            
            System.out.println("✅ Notification email sent to: " + toEmail);
        } catch (Exception e) {
            System.err.println("❌ Failed to send notification email: " + e.getMessage());
        }
    }
}

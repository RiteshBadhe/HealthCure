package healthcare_backend.com.example.demo.controller;

import healthcare_backend.com.example.demo.annotation.RequireRole;
import healthcare_backend.com.example.demo.model.Notification;
import healthcare_backend.com.example.demo.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/notifications")
@CrossOrigin(origins = "http://localhost:3000")
public class NotificationController {
    
    @Autowired
    private NotificationRepository notificationRepository;
    
    // Get all notifications for a user
    @GetMapping("/user/{userId}")
    @RequireRole({"PATIENT", "DOCTOR", "ADMIN"})
    public ResponseEntity<List<Notification>> getUserNotifications(@PathVariable Long userId) {
        List<Notification> notifications = notificationRepository.findByUserIdOrderByCreatedAtDesc(userId);
        return ResponseEntity.ok(notifications);
    }
    
    // Get unread notifications for a user
    @GetMapping("/user/{userId}/unread")
    @RequireRole({"PATIENT", "DOCTOR", "ADMIN"})
    public ResponseEntity<List<Notification>> getUnreadNotifications(@PathVariable Long userId) {
        List<Notification> notifications = notificationRepository.findByUserIdAndIsReadFalseOrderByCreatedAtDesc(userId);
        return ResponseEntity.ok(notifications);
    }
    
    // Get unread count
    @GetMapping("/user/{userId}/unread-count")
    @RequireRole({"PATIENT", "DOCTOR", "ADMIN"})
    public ResponseEntity<Map<String, Long>> getUnreadCount(@PathVariable Long userId) {
        Long count = notificationRepository.countByUserIdAndIsReadFalse(userId);
        Map<String, Long> response = new HashMap<>();
        response.put("count", count);
        return ResponseEntity.ok(response);
    }
    
    // Get a specific notification
    @GetMapping("/{id}")
    @RequireRole({"PATIENT", "DOCTOR", "ADMIN"})
    public ResponseEntity<Notification> getNotificationById(@PathVariable Long id) {
        Optional<Notification> notification = notificationRepository.findById(id);
        return notification.map(ResponseEntity::ok)
                         .orElse(ResponseEntity.notFound().build());
    }
    
    // Create a new notification
    @PostMapping
    @RequireRole({"DOCTOR", "ADMIN"})
    public ResponseEntity<Notification> createNotification(@RequestBody Notification notification) {
        Notification savedNotification = notificationRepository.save(notification);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedNotification);
    }
    
    // Mark notification as read
    @PutMapping("/{id}/read")
    @RequireRole({"PATIENT", "DOCTOR", "ADMIN"})
    public ResponseEntity<Notification> markAsRead(@PathVariable Long id) {
        Optional<Notification> notificationOpt = notificationRepository.findById(id);
        if (notificationOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        
        Notification notification = notificationOpt.get();
        notification.setIsRead(true);
        Notification updatedNotification = notificationRepository.save(notification);
        return ResponseEntity.ok(updatedNotification);
    }
    
    // Mark all notifications as read for a user
    @PutMapping("/user/{userId}/read-all")
    @RequireRole({"PATIENT", "DOCTOR", "ADMIN"})
    public ResponseEntity<Map<String, String>> markAllAsRead(@PathVariable Long userId) {
        List<Notification> unreadNotifications = notificationRepository.findByUserIdAndIsReadFalse(userId);
        for (Notification notification : unreadNotifications) {
            notification.setIsRead(true);
        }
        notificationRepository.saveAll(unreadNotifications);
        
        Map<String, String> response = new HashMap<>();
        response.put("message", "All notifications marked as read");
        response.put("count", String.valueOf(unreadNotifications.size()));
        return ResponseEntity.ok(response);
    }
    
    // Delete a notification
    @DeleteMapping("/{id}")
    @RequireRole({"PATIENT", "DOCTOR", "ADMIN"})
    public ResponseEntity<Void> deleteNotification(@PathVariable Long id) {
        if (!notificationRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        notificationRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
    
    // Delete all notifications for a user
    @DeleteMapping("/user/{userId}")
    @RequireRole({"PATIENT", "DOCTOR", "ADMIN"})
    public ResponseEntity<Void> deleteAllUserNotifications(@PathVariable Long userId) {
        List<Notification> notifications = notificationRepository.findByUserId(userId);
        notificationRepository.deleteAll(notifications);
        return ResponseEntity.noContent().build();
    }
}

package healthcare_backend.com.example.demo.repository;

import healthcare_backend.com.example.demo.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    
    // Find all notifications for a user
    List<Notification> findByUserId(Long userId);
    
    // Find unread notifications for a user
    List<Notification> findByUserIdAndIsReadFalse(Long userId);
    
    // Find by user ordered by creation date descending
    List<Notification> findByUserIdOrderByCreatedAtDesc(Long userId);
    
    // Find unread notifications ordered by creation date
    List<Notification> findByUserIdAndIsReadFalseOrderByCreatedAtDesc(Long userId);
    
    // Find by type
    List<Notification> findByUserIdAndType(Long userId, String type);
    
    // Count unread notifications
    Long countByUserIdAndIsReadFalse(Long userId);
}

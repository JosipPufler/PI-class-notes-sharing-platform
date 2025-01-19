package hr.algebra.pi.repositories;

import hr.algebra.pi.models.NotificationType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationTypeRepo extends JpaRepository<NotificationType, Long> {
}

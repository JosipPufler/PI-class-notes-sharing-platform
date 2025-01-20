package hr.algebra.pi.repositories;

import hr.algebra.pi.models.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepo extends JpaRepository<Notification, Long> {
    default List<Notification> findByUserId(long userId){
        return findAll().stream().filter(x -> x.getUser().getId() == userId).toList();
    }
}

package hr.algebra.pi.services;

import hr.algebra.pi.models.Notification;
import hr.algebra.pi.models.User;
import hr.algebra.pi.repositories.NotificationRepo;
import hr.algebra.pi.services.interfaces.IDatabaseService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class NotificationService implements IDatabaseService<Notification> {
    final NotificationRepo repo;

    @Autowired
    NotificationTypeService notificationTypeService;


    public NotificationService(NotificationRepo repo) {
        this.repo = repo;
    }

    @Override
    public Notification findById(Long id) throws EntityNotFoundException {
        return repo.findById(id).orElseThrow(() -> new EntityNotFoundException("Notification with id: '" + id + "' not found"));
    }

    @Override
    public void update(Notification entity) {
        repo.saveAndFlush(entity);
    }

    @Override
    public void deleteById(Long id) {
        repo.deleteById(id);
        repo.flush();
    }

    public void deleteAll() {
        repo.deleteAll();
        repo.flush();
    }

    @Override
    public List<Notification> getAll() {
        return repo.findAll();
    }

    @Override
    public Notification create(Notification entity) {
        entity.setIsRead(false);
        return repo.saveAndFlush(entity);
    }

    public Notification createInfoNotification(String title, String content, User user) {
        Notification notification = new Notification();
        notification.setNotificationType(notificationTypeService.findByName("Info"));
        notification.setContent(content);
        notification.setUser(user);
        notification.setTime(LocalDateTime.now());
        notification.setTitle(title);
        return create(notification);
    }

    public List<Notification> findByUserId(Long userId) {
        return repo.findByUserId(userId);
    }

    public void markAsRead(Long id) {
        Notification notification = findById(id);
        notification.setIsRead(true);
        repo.saveAndFlush(notification);
    }

    public void clearAllUserNotifications(Long userId) {
        for (Notification notification : findByUserId(userId)){
            deleteById(notification.getId());
        }
    }
}

package hr.algebra.pi.services;

import hr.algebra.pi.models.NotificationType;
import hr.algebra.pi.repositories.NotificationTypeRepo;
import hr.algebra.pi.services.interfaces.IDatabaseService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationTypeService implements IDatabaseService<NotificationType> {
    final NotificationTypeRepo notificationTypeRepo;

    public NotificationTypeService(NotificationTypeRepo notificationTypeRepo) {
        this.notificationTypeRepo = notificationTypeRepo;
    }

    @Override
    public NotificationType findById(Long id) {
        return notificationTypeRepo.findById(id).orElseThrow(() -> new EntityNotFoundException("Notification type with id: '" + id + "' not found"));
    }

    public NotificationType findByName(String name) {
        for(NotificationType notificationType : notificationTypeRepo.findAll()){
            if (notificationType.getName().equals(name))
                return notificationType;
        }
        throw new EntityNotFoundException("No notification type with name: " + name);
    }

    @Override
    public void update(NotificationType entity) {
        notificationTypeRepo.saveAndFlush(entity);
    }

    @Override
    public void deleteById(Long id) {
        notificationTypeRepo.deleteById(id);
        notificationTypeRepo.flush();
    }

    public void deleteAll() {
        notificationTypeRepo.deleteAll();
        notificationTypeRepo.flush();
    }

    @Override
    public List<NotificationType> getAll() {
        return notificationTypeRepo.findAll();
    }

    @Override
    public NotificationType create(NotificationType entity) {
        return notificationTypeRepo.saveAndFlush(entity);
    }
}

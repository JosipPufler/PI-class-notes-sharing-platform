package hr.algebra.pi.controllers;

import hr.algebra.pi.models.NotificationType;
import hr.algebra.pi.services.NotificationTypeService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping(path="api/notificationType")
public class NotificationTypeController {
    private final NotificationTypeService notificationTypeService;

    public NotificationTypeController(NotificationTypeService notificationTypeService) {
        this.notificationTypeService = notificationTypeService;
    }

    @GetMapping
    public ResponseEntity<List<NotificationType>> getAllNotificationTypes() {
        return new ResponseEntity<>(notificationTypeService.getAll(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<NotificationType> getNotificationType(@PathVariable String id) {
        try {
            return new ResponseEntity<>(notificationTypeService.findById(Long.parseLong(id)), HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}

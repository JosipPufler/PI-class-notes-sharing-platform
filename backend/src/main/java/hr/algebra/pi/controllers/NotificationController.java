package hr.algebra.pi.controllers;

import hr.algebra.pi.models.Notification;
import hr.algebra.pi.services.NotificationService;
import hr.algebra.pi.services.UserService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping(path="api/notification")
public class NotificationController {
    final NotificationService notificationService;
    final UserService userService;

    public NotificationController(NotificationService notificationService, UserService userService) {
        this.notificationService = notificationService;
        this.userService = userService;
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{id}")
    ResponseEntity<List<Notification>> getAllUserNotifications(@PathVariable String id) {
        try {
            return new ResponseEntity<>(notificationService.findByUserId(Long.parseLong(id)), HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PreAuthorize("isAuthenticated()")
    @PutMapping("/{id}")
    ResponseEntity<Void> markNotificationAsRead(@PathVariable String id) {
        try {
            notificationService.markAsRead(Long.parseLong(id));
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/clear/{id}")
    ResponseEntity<Void> clearNotifications(@PathVariable String id) {
        try {
            notificationService.clearAllUserNotifications(Long.parseLong(id));
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/{id}")
    ResponseEntity<Void> deleteNotification(@PathVariable String id) {
        try {
            notificationService.deleteById(Long.parseLong(id));
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (EntityNotFoundException e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}

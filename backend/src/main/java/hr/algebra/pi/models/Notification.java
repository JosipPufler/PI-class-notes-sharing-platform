package hr.algebra.pi.models;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class Notification {
    @Id
    @Column(name = "id", unique=true, nullable=false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @Column(name = "time", nullable = false)
    LocalDateTime time;
    @Column(name = "title", nullable = false)
    String title;
    @Column(name = "content")
    String content;
    @Column(name = "is_read", nullable = false)
    Boolean isRead = false;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_Id", nullable = false)
    User user;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "notification_type_id", nullable = false)
    NotificationType notificationType;
}

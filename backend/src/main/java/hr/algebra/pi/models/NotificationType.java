package hr.algebra.pi.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "notification_type")
public class NotificationType {
    @Id
    @Column(name="id", unique=true, nullable=false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @Column(name = "name", unique = true, nullable = false)
    String name;

    @JsonIgnore
    @OneToMany(mappedBy = "notificationType", cascade = CascadeType.REMOVE, orphanRemoval = true)
    Set<Notification> notifications;
}

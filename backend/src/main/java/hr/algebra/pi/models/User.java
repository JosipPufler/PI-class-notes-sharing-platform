package hr.algebra.pi.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Setter
@Getter
@Entity
@AllArgsConstructor
@Table(name= "user", schema = "public")
@NoArgsConstructor
public class User {
    @Id
    @Column(name="id", unique=true, nullable=false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @Column(name="username", unique=true, nullable=false)
    String username;
    @Column(name="passwordHash", nullable=false)
    String passwordHash;
    @Column(name="firstname")
    String firstName;
    @Column(name="lastname")
    String lastName;
    @Column(name="email")
    String email;
    @Column(name="phoneNumber")
    String phoneNumber;
    @Column(name="active")
    boolean active = true;
    @Column(name="settings")
    String settings;
    @ManyToMany
    @JoinTable(
            name = "UserInterest",
            joinColumns = @JoinColumn(name = "userId"),
            inverseJoinColumns = @JoinColumn(name = "interestId"))
    public Set<Interest> interests = new HashSet<>();
    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE, orphanRemoval = true)
    Set<TwoFactorAuthenticationEntry> twoFactorAuthenticationEntries;
    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE, orphanRemoval = true)
    Set<Notification> notifications;

}

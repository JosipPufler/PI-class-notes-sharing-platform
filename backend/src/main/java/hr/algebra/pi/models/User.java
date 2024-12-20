package hr.algebra.pi.models;

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
    private Long id;
    @Column(name="username", unique=true, nullable=false)
    String username;
    @Column(name="passwordhash", nullable=false)
    String passwordHash;
    @Column(name="passwordsalt", nullable=false)
    String passwordSalt;
    @Column(name="firstname")
    String firstName;
    @Column(name="lastname")
    String lastName;
    @Column(name="email")
    String email;
    @Column(name="phoneNumber")
    String phoneNumber;
    @Column(name="active")
    boolean active;
    @Column(name="settings")
    String settings;
    @ManyToMany
    @JoinTable(
            name = "UserInterest",
            joinColumns = @JoinColumn(name = "userId"),
            inverseJoinColumns = @JoinColumn(name = "interestId"))
    public Set<Interest> interests = new HashSet<>();

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", passwordHash=" + passwordHash +
                ", passwordSalt=" + passwordSalt +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                '}';
    }
}

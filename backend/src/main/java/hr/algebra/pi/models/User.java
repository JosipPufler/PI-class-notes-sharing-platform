package hr.algebra.pi.models;

import hr.algebra.pi.services.PasswordService;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Set;

@Setter
@Getter
@Entity
@AllArgsConstructor
@Table(name="Users")
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
    @Column(name="phonenumber")
    String phoneNumber;
    @ManyToMany
    @JoinTable(
            name = "UserInterest",
            joinColumns = @JoinColumn(name = "userId"),
            inverseJoinColumns = @JoinColumn(name = "interestId"))
    public Set<Interest> interests;

    public User(SignInForm signInForm) {
        this.username = signInForm.getUsername();
        this.firstName = signInForm.getFirstName();
        this.lastName = signInForm.getLastName();
        this.email = signInForm.getEmail();
        this.phoneNumber = signInForm.getPhoneNumber();

        this.passwordSalt = PasswordService.generateNewSalt();
        this.passwordHash = PasswordService.hashStringWithSalt(signInForm.password, this.passwordSalt.getBytes(StandardCharsets.UTF_8));
    }

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

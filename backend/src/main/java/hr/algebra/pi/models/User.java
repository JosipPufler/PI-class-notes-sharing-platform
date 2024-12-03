package hr.algebra.pi.models;

import hr.algebra.pi.services.PasswordService;
import jakarta.persistence.*;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

@Entity
@Table(name="Users")
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

    public User() {}

    public User(Long id, String username, String passwordHash, String passwordSalt, String firstName, String lastName, String email, String phoneNumber) {
        this.id = id;
        this.username = username;
        this.passwordHash = passwordHash;
        this.passwordSalt = passwordSalt;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }

    public User(SignInForm signInForm) {
        this.username = signInForm.getUsername();
        this.firstName = signInForm.getFirstName();
        this.lastName = signInForm.getLastName();
        this.email = signInForm.getEmail();
        this.phoneNumber = signInForm.getPhoneNumber();

        this.passwordSalt = PasswordService.generateNewSalt();
        this.passwordHash = PasswordService.hashStringWithSalt(signInForm.password, this.passwordSalt.getBytes(StandardCharsets.UTF_8));
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
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

package hr.algebra.pi.models;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SignInForm {
    String username;
    String password;
    String email;
    String phoneNumber;
    String firstName;
    String lastName;

    SignInForm (String username, String password, String email, String phoneNumber, String firstName, String lastName) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    @Override
    public String toString() {
        return "SignInForm{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                '}';
    }
}

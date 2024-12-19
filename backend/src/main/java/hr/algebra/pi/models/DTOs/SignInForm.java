package hr.algebra.pi.models.DTOs;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Setter
@Getter
public class SignInForm {
    String username;
    String password;
    String email;
    String phoneNumber;
    String firstName;
    String lastName;
    List<Long> interests;

    SignInForm(String username, String password, String email, String phoneNumber, String firstName, String lastName){
        this(username, password, email, phoneNumber, firstName, lastName, new ArrayList<>());
    }

    SignInForm (String username, String password, String email, String phoneNumber, String firstName, String lastName, ArrayList<String> interests) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.firstName = firstName;
        this.lastName = lastName;
        this.interests = interests.stream().map(Long::parseLong).toList();
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

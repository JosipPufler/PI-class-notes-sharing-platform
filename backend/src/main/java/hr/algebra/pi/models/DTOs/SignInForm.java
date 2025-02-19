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
    List<Long> interests = new ArrayList<>();

    public SignInForm(String username, String password, String email, String phoneNumber, String firstName, String lastName){
        this(username, password, email, phoneNumber, firstName, lastName, new ArrayList<>());
    }

    public SignInForm (String username, String password, String email, String phoneNumber, String firstName, String lastName, List<String> interests) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.firstName = firstName;
        this.lastName = lastName;
        this.interests = interests.stream().map(Long::parseLong).toList();
    }
}

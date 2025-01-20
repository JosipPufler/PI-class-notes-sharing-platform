package hr.algebra.pi.models.DTOs;

import hr.algebra.pi.models.Interest;
import hr.algebra.pi.models.UserSettings;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO implements Serializable {
    Long id;
    String username;
    String firstName;
    String lastName;
    String email;
    String phoneNumber;
    List<Interest> interests = new ArrayList<>();
    Boolean active;
    UserSettings settings;
}

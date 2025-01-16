package hr.algebra.pi.models.DTOs;

import hr.algebra.pi.models.Interest;
import hr.algebra.pi.models.UserSettings;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class UserDTO {
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

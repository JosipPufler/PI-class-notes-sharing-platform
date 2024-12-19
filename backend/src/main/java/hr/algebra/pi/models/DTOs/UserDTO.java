package hr.algebra.pi.models.DTOs;

import jakarta.persistence.Column;
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
    List<InterestDTO> interests = new ArrayList<>();
    Boolean active;
}

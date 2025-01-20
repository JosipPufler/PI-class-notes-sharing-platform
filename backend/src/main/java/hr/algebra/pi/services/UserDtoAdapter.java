package hr.algebra.pi.services;

import com.fasterxml.jackson.annotation.JsonIgnore;
import hr.algebra.pi.models.DTOs.UserDTO;
import hr.algebra.pi.models.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
public class UserDtoAdapter extends UserDTO implements Serializable {
    public UserDtoAdapter(User user){
        this.setActive(user.isActive());
        this.setId(user.getId());
        this.setEmail(user.getEmail());
        this.setFirstName(user.getFirstName());
        this.setLastName(user.getLastName());
        this.setUsername(user.getUsername());
        this.setPhoneNumber(user.getPhoneNumber());
        this.setInterests(user.getInterests().stream().toList());
    }
}

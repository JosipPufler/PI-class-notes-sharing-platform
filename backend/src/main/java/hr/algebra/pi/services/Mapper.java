package hr.algebra.pi.services;

import hr.algebra.pi.models.DTOs.InterestDTO;
import hr.algebra.pi.models.DTOs.UserDTO;
import hr.algebra.pi.models.Interest;
import hr.algebra.pi.models.DTOs.SignInForm;
import hr.algebra.pi.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Optional;

@Component
public class Mapper {
    final UserService userService;
    final InterestService interestService;
    final PasswordEncoder passwordEncoder;

    @Autowired
    public Mapper(UserService userService, InterestService interestService) {
        this.userService = userService;
        this.interestService = interestService;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    public User mapSignInFormToUser(SignInForm signInForm) {
        User user = new User();
        user.setUsername(signInForm.getUsername());
        user.setFirstName(signInForm.getFirstName());
        user.setLastName(signInForm.getLastName());
        user.setEmail(signInForm.getEmail());
        user.setPhoneNumber(signInForm.getPhoneNumber());

        if (signInForm.getPassword() != null && !signInForm.getPassword().isEmpty()) {
            user.setPasswordHash(passwordEncoder.encode(signInForm.getPassword()));
        }else{
            user.setPasswordHash(null);
        }

        for (Long id : signInForm.getInterests()) {
            Optional<Interest> interest = interestService.getInterest(id);
            interest.ifPresent(value -> user.getInterests().add(value));
        }
        return user;
    }

    public UserDTO mapToUserDTO(User user){
        UserDTO userDTO = new UserDTO();
        userDTO.setEmail(user.getEmail());
        userDTO.setFirstName(user.getFirstName());
        userDTO.setLastName(user.getLastName());
        userDTO.setPhoneNumber(user.getPhoneNumber());
        userDTO.setUsername(user.getUsername());
        for (Interest interest : user.getInterests()) {
            InterestDTO interestDTO = mapToInterestDTO(interest);
            userDTO.getInterests().add(interestDTO);
        }
        userDTO.setActive(user.isActive());
        userDTO.setId(user.getId());
        return userDTO;
    }

    public InterestDTO mapToInterestDTO(Interest interest){
        InterestDTO interestDTO = new InterestDTO();
        interestDTO.setId(interest.getId());
        interestDTO.setName(interest.getName());
        if (interest.getParentInterest() != null) {
            interestDTO.setParentInterest(interest.getParentInterest().getName());
        } else {
            interestDTO.setParentInterest(null);
        }
        return interestDTO;
    }
}

package hr.algebra.pi.services;

import hr.algebra.pi.models.DTOs.UserDTO;
import hr.algebra.pi.models.Interest;
import hr.algebra.pi.models.DTOs.SignInForm;
import hr.algebra.pi.models.User;
import hr.algebra.pi.models.UserSettings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

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
        user.setActive(true);

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
        userDTO.setInterests(user.getInterests().stream().toList());
        if (user.getSettings() == null || user.getSettings().isEmpty()) {
            userDTO.setSettings(new UserSettings());
        } else {
            userDTO.setSettings(SettingsService.JsonToUserSettings(user.getSettings()));
        }
        userDTO.setActive(user.isActive());
        userDTO.setId(user.getId());
        return userDTO;
    }
}

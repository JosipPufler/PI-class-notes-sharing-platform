package hr.algebra.pi.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
    static final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    public Mapper(UserService userService, InterestService interestService) {
        this.userService = userService;
        this.interestService = interestService;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    public User mapSignInFormToUser(SignInForm signInForm) {
        User user = UserBuilder.start()
                .username(signInForm.getUsername())
                .email(signInForm.getEmail())
                .phoneNumber(signInForm.getPhoneNumber())
                .firstName(signInForm.getFirstName())
                .lastName(signInForm.getLastName())
                .active(true).build();

        if (signInForm.getPassword() != null && !signInForm.getPassword().isEmpty()) {
            user.setPasswordHash(passwordEncoder.encode(signInForm.getPassword()));
        } else {
            user.setPasswordHash(null);
        }

        for (Long id : signInForm.getInterests()) {
            Optional<Interest> interest = interestService.getInterest(id);
            interest.ifPresent(value -> user.getInterests().add(value));
        }
        return user;
    }

    public static String userSettingsToJson(UserSettings userSettings){
        try {
            return objectMapper.writeValueAsString(userSettings);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public static UserSettings jsonToUserSettings(String json){
        try {
            return objectMapper.readValue(json, UserSettings.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}

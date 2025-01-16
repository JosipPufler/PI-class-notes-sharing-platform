package hr.algebra.pi.controllers;

import hr.algebra.pi.models.DTOs.*;
import hr.algebra.pi.models.TwoFactorAuthenticationEntry;
import hr.algebra.pi.models.User;
import hr.algebra.pi.models.UserSettings;
import hr.algebra.pi.services.*;
import hr.algebra.pi.services.interfaces.IUserService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@CrossOrigin
@RestController
@RequestMapping(path="api/user")
public class UserController {
    final IUserService userService;
    final Mapper mapper;
    final JwtService jwtService;
    final AuthenticationManager authenticationManager;
    final Random rand = new Random();
    final TwoFAService twoFAService;
    final MailService mailService;

    @Autowired
    public UserController(UserService userService, Mapper mapper, JwtService jwtService, AuthenticationManager authenticationManager, TwoFAService twoFAService, MailService mailService) {
        this.mapper = mapper;
        this.userService = userService;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.twoFAService = twoFAService;
        this.mailService = mailService;
    }

    @GetMapping
    public ResponseEntity<List<UserDTO>> getUsers() {
        List<UserDTO> users = new ArrayList<>();
        for (User user : userService.getAll()) {
            users.add(mapper.mapToUserDTO(user));
        }
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUser(@PathVariable Long id) {
        User user = userService.findById(id);
        return new ResponseEntity<>(mapper.mapToUserDTO(user), HttpStatus.OK);
    }

    @PostMapping("/signIn")
    public ResponseEntity<UserDTO> addUser(@RequestBody SignInForm signInForm) {
        User newUser = userService.create(mapper.mapSignInFormToUser(signInForm));
        return new ResponseEntity<>(mapper.mapToUserDTO(newUser), HttpStatus.CREATED);
    }

    @PostMapping("/logIn")
    public ResponseEntity<LogInResponse> login(@RequestBody LogInForm logInForm) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(logInForm.getUsername(), logInForm.getPassword()));

        SecurityContextHolder.getContext()
                .setAuthentication(authentication);
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        User user = userService.findById(userDetails.getId());

        if (user == null || !user.isActive()) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        if (SettingsService.JsonToUserSettings(user.getSettings()).getTwoFactorAuthenticationEnabled()){
            String code = String.format("%04d", rand.nextInt(10000));
            TwoFactorAuthenticationEntry entry = new TwoFactorAuthenticationEntry();
            entry.setUser(user);
            entry.setCode(code);
            entry.setExpiryDateTime(LocalDateTime.now().plusMinutes(5));
            twoFAService.create(entry);
            mailService.SendMail(user.getEmail(), "2 step verification", mailService.Generate2FAMessage(user, code));
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        String jwt = jwtService.generateJwtToken(authentication);
        return new ResponseEntity<>(new LogInResponse(jwt, userDetails.getUsername(), userDetails.getId()), HttpStatus.OK);
    }

    @PostMapping("/confirmLogIn")
    public ResponseEntity<LogInResponse> confirmLogIn(@RequestBody LogInFormWithAuthentication logInForm) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(logInForm.getUsername(), logInForm.getPassword()));

        SecurityContextHolder.getContext()
                .setAuthentication(authentication);
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        Optional<TwoFactorAuthenticationEntry> entry = twoFAService.getByUserId(userDetails.getId());
        if (entry.isPresent() && entry.get().getCode().equals(logInForm.getCode())) {
            String jwt = jwtService.generateJwtToken(authentication);
            twoFAService.deleteAllUserEntries(userDetails.getId());
            return new ResponseEntity<>(new LogInResponse(jwt, userDetails.getUsername(), userDetails.getId()), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateUser(@PathVariable Long id, @RequestBody SignInForm signInForm) {
        try {
            User user = mapper.mapSignInFormToUser(signInForm);
            if(user.getPasswordHash() == null) {
                User existingUser = userService.findById(id);
                user.setPasswordHash(existingUser.getPasswordHash());
            }
            user.setId(id);
            userService.update(user);
            return new ResponseEntity<>("\"Success\"", HttpStatus.OK);
        } catch (EntityNotFoundException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/updateSettings/{id}")
    public ResponseEntity<Boolean> updateUserSettings(@PathVariable Long id, @RequestBody UserSettings userSettings) {
        try {
            User user = userService.findById(id);
            user.setSettings(SettingsService.UserSettingsToJson(userSettings));
            userService.update(user);
            return new ResponseEntity<>(true, HttpStatus.OK);
        } catch (EntityNotFoundException e){
            return new ResponseEntity<>(false, HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deleteUser(@PathVariable Long id){
        if (userService.deactivateUser(id))
            return new ResponseEntity<>(true, HttpStatus.OK);
        return new ResponseEntity<>(false, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<String> dataIntegrityViolationException(final DataIntegrityViolationException e) {
        if (e.getMessage().toLowerCase().contains("users_phonenumber_key")){
            return new ResponseEntity<>("\"phone\"", HttpStatus.CONFLICT);
        }
        else if (e.getMessage().toLowerCase().contains("users_email_key")){
            return new ResponseEntity<>("\"email\"", HttpStatus.CONFLICT);
        }
        else if (e.getMessage().toLowerCase().contains("users_username_key")){
            return new ResponseEntity<>("\"username\"", HttpStatus.CONFLICT);
        }
        return new ResponseEntity<>("\"general\"", HttpStatus.CONFLICT);
    }
}

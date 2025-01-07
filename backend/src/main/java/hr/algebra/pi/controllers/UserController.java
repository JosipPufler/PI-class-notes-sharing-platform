package hr.algebra.pi.controllers;

import hr.algebra.pi.models.DTOs.LogInForm;
import hr.algebra.pi.models.DTOs.LogInResponse;
import hr.algebra.pi.models.DTOs.SignInForm;
import hr.algebra.pi.models.DTOs.UserDTO;
import hr.algebra.pi.models.User;
import hr.algebra.pi.services.JwtService;
import hr.algebra.pi.services.Mapper;
import hr.algebra.pi.services.UserDetailsImpl;
import hr.algebra.pi.services.UserService;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@CrossOrigin
@RestController
@RequestMapping(path="api/user")
public class UserController {
    final UserService userService;
    final Mapper mapper;
    final JwtService jwtService;
    final AuthenticationManager authenticationManager;


    @Autowired
    public UserController(UserService userService, Mapper mapper, JwtService jwtService, AuthenticationManager authenticationManager) {
        this.mapper = mapper;
        this.userService = userService;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    @GetMapping
    public ResponseEntity<List<UserDTO>> getUsers() {
        List<UserDTO> users = new ArrayList<>();
        for (User user : userService.getAllUsers()) {
            users.add(mapper.mapToUserDTO(user));
        }
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUsers(@PathVariable Long id) {
        Optional<User> user = userService.getUser(id);
        return user.map(value -> new ResponseEntity<>(mapper.mapToUserDTO(value), HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping("/signIn")
    public ResponseEntity<UserDTO> addUser(@RequestBody SignInForm signInForm) {
        System.out.println(signInForm.toString());
        User user = mapper.mapSignInFormToUser(signInForm);
        System.out.println(user);
        User newUser = userService.createUser(user);
        return new ResponseEntity<>(mapper.mapToUserDTO(newUser), HttpStatus.CREATED);
    }

    @PostMapping("/logIn")
    public ResponseEntity<LogInResponse> login(@RequestBody LogInForm logInForm) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(logInForm.getUsername(), logInForm.getPassword()));

        SecurityContextHolder.getContext()
                .setAuthentication(authentication);
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        String jwt = jwtService.generateJwtToken(authentication);

        if (userService.getUser(userDetails.getId()).isPresent() && userService.getUser(userDetails.getId()).get().isActive()) {
            return new ResponseEntity<>(new LogInResponse(jwt, userDetails.getUsername(), userDetails.getId()), HttpStatus.OK);
        } else
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateUser(@PathVariable Long id, @RequestBody SignInForm signInForm) {
        try {
            User user = mapper.mapSignInFormToUser(signInForm);
            if(user.getPasswordHash() == null) {
                Optional<User> existingUser = userService.getUser(id);
                if(existingUser.isEmpty()) {
                    return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
                }
                user.setPasswordHash(existingUser.get().getPasswordHash());
            }
            user.setId(id);
            userService.updateUser(user);
            return new ResponseEntity<>("\"Success\"", HttpStatus.OK);
        } catch (EntityNotFoundException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deleteUser(@PathVariable Long id){
        Boolean ok = userService.deactivateUser(id);
        if (ok)
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

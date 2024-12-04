package hr.algebra.pi.controllers;

import hr.algebra.pi.models.SignInForm;
import hr.algebra.pi.models.User;
import hr.algebra.pi.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping(path="api/user")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<User>> getUsers() {
        return new ResponseEntity<>(userService.getAllUsers(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<User> addUser(@RequestBody SignInForm signInForm) {
        System.out.println(signInForm.toString());
        User user = new User(signInForm);
        System.out.println(user);
        User newUser = userService.createUser(user);
        return new ResponseEntity<>(newUser, HttpStatus.CREATED);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<String> dataIntegrityViolantionException(final DataIntegrityViolationException e) {
        String mostSpecificCauseMessage = e.getMostSpecificCause().getMessage();
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

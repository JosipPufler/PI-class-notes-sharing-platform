package hr.algebra.pi.controllers;

import hr.algebra.pi.models.Interest;
import hr.algebra.pi.services.InterestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin
@RestController
@RequestMapping(path="api/interest")
public class InterestController {
    private final InterestService interestService;

    @Autowired
    public InterestController(InterestService interestService) {
        this.interestService = interestService;
    }

    @GetMapping
    public ResponseEntity<List<Interest>> getInterests() {
        return new ResponseEntity<>(interestService.getAllInterests(), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<Interest> getInterest(@RequestBody Long id) {
        Optional<Interest> interest = interestService.getInterest(id);
        return interest.map(value -> new ResponseEntity<>(value, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public ResponseEntity<Interest> addInterest(@RequestBody Interest interest) {
        System.out.println(interest);
        Interest newInterest = interestService.createInterest(interest);
        System.out.println(newInterest);
        return new ResponseEntity<>(newInterest, HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<Interest> updateInterest(@RequestBody Interest interest) {
        Interest updatedInterest = interestService.updateInterest(interest);
        return new ResponseEntity<>(updatedInterest, HttpStatus.CREATED);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<String> dataIntegrityViolantionException(final DataIntegrityViolationException e) {
        String mostSpecificCauseMessage = e.getMostSpecificCause().getMessage();
        if (e.getMessage().toLowerCase().contains("interests_name_key")){
            return new ResponseEntity<>("\"name\"", HttpStatus.CONFLICT);
        }
        return new ResponseEntity<>("\"general\"", HttpStatus.CONFLICT);
    }
}

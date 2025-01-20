package hr.algebra.pi.controllers;

import hr.algebra.pi.models.Interest;
import hr.algebra.pi.models.DTOs.InterestCreationForm;
import hr.algebra.pi.services.InterestService;
import hr.algebra.pi.services.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin
@RestController
@RequestMapping(path="api/interest")
public class InterestController {
    private final InterestService interestService;

    @Autowired
    public InterestController(InterestService interestService, Mapper mapper) {
        this.interestService = interestService;
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping
    public ResponseEntity<List<Interest>> getInterests() {
        return new ResponseEntity<>(interestService.getAllInterests(), HttpStatus.OK);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{id}")
    public ResponseEntity<Interest> getInterest(@PathVariable Long id) {
        Optional<Interest> interest = interestService.getInterest(id);
        return interest.map(value -> new ResponseEntity<>(value, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping
    public ResponseEntity<Interest> addInterest(@RequestBody InterestCreationForm interestCreationForm) {
        Interest newInterest = new Interest();
        newInterest.setName(interestCreationForm.getName());
        if (interestCreationForm.getParentInterestId() != null) {
            Optional<Interest> parentInterest = interestService.getInterest(interestCreationForm.getParentInterestId());
            if (parentInterest.isPresent()) {
                newInterest.setParentInterest(parentInterest.get());
            }else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        }

        interestService.createInterest(newInterest);
        return new ResponseEntity<>(newInterest, HttpStatus.CREATED);
    }

    @PreAuthorize("isAuthenticated()")
    @PutMapping
    public ResponseEntity<Interest> updateInterest(@RequestBody Interest interest) {
        Interest updatedInterest = interestService.updateInterest(interest);
        return new ResponseEntity<>(updatedInterest, HttpStatus.OK);
    }

    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deleteInterest(@PathVariable Long id) {
        interestService.deleteInterest(id);
        return new ResponseEntity<>(true, HttpStatus.OK);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<String> dataIntegrityViolationException(final DataIntegrityViolationException e) {
        return new ResponseEntity<>("\"name\"", HttpStatus.CONFLICT);
    }
}

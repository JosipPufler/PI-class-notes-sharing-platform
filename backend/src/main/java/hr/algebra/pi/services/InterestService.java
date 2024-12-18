package hr.algebra.pi.services;

import hr.algebra.pi.models.Interest;
import hr.algebra.pi.repositories.InterestRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Component
@Transactional
public class InterestService {
    private final InterestRepo interestRepo;

    @Autowired
    public InterestService(InterestRepo interestRepo) {
        this.interestRepo = interestRepo;
    }

    public Interest createInterest(Interest interest) {
        return interestRepo.saveAndFlush(interest);
    }

    public List<Interest> getAllInterests() {
        return interestRepo.findAll();
    }

    public Optional<Interest> getInterest(Long id) {return interestRepo.findById(id);}

    public Interest updateInterest(Interest interest) { return interestRepo.saveAndFlush(interest); }
}

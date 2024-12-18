package hr.algebra.pi.services;

import hr.algebra.pi.models.Interest;
import hr.algebra.pi.models.User;
import hr.algebra.pi.repositories.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Component
@Transactional
public class UserService {
    private final UserRepo userRepo;
    private final InterestService interestService;

    @Autowired
    public UserService(UserRepo userRepo, InterestService interestService) {
        this.userRepo = userRepo;
        this.interestService = interestService;
    }

    public User createUser(User user) {
        return userRepo.saveAndFlush(user);
    }

    public List<User> getAllUsers() {
        return userRepo.findAll();
    }

    public User addInterest(Long userId, Long interestId) {
        Optional<User> user = userRepo.findById(userId);
        if (user.isPresent()) {
            Optional<Interest> interest = interestService.getInterest(interestId);
            if (interest.isPresent()) {
                user.get().getInterests().add(interest.get());
                userRepo.saveAndFlush(user.get());
                return user.get();
            }else{
                throw new RuntimeException("Interest not found");
            }
        } else {
            throw new RuntimeException("User not found");
        }
    }
}

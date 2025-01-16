package hr.algebra.pi.services;

import hr.algebra.pi.models.User;
import hr.algebra.pi.models.UserSettings;
import hr.algebra.pi.repositories.UserRepo;
import hr.algebra.pi.services.interfaces.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Component
@Transactional
public class UserService implements IUserService {
    private final UserRepo userRepo;

    @Autowired
    public UserService(UserRepo userRepo, InterestService interestService) {
        this.userRepo = userRepo;
    }

    @Override
    public User findByUsername(String username) {
        return userRepo.findByUsername(username).orElseThrow(() -> new RuntimeException("User with username: '" + username + "' not found"));
    }

    @Override
    public Boolean deactivateUser(Long id) {
        Optional<User> user = userRepo.findById(id);
        if (user.isPresent()) {
            user.get().setActive(false);
            return true;
        }
        return false;
    }

    @Override
    public User findById(Long id) {
        return userRepo.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
    }

    @Override
    public void update(User entity) {
        userRepo.saveAndFlush(entity);
    }

    @Override
    public void deleteById(Long id) {
        userRepo.deleteById(id);
    }

    @Override
    public List<User> getAll() {
        return userRepo.findAll();
    }

    @Override
    public User create(User entity) {
        entity.setActive(true);
        entity.setSettings(SettingsService.UserSettingsToJson(new UserSettings()));
        return userRepo.saveAndFlush(entity);
    }
}

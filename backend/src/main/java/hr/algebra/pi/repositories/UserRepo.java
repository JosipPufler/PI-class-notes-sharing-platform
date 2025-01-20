package hr.algebra.pi.repositories;

import hr.algebra.pi.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface UserRepo extends JpaRepository<User, Long> {
    default Optional<User> findByUsername(String username){
        return findAll().stream().filter(user -> username.equals(user.getUsername())).findFirst();
    }
}

package hr.algebra.pi.repositories;

import hr.algebra.pi.models.Group;
import hr.algebra.pi.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface GroupRepo extends JpaRepository<Group, Long> {
    List<Group> findByUsersContaining(User user);
}

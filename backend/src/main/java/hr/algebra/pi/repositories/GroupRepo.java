package hr.algebra.pi.repositories;

import hr.algebra.pi.models.Group;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GroupRepo extends JpaRepository<Group, Long> {
}

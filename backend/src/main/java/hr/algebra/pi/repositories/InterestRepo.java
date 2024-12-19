package hr.algebra.pi.repositories;

import hr.algebra.pi.models.Interest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface InterestRepo extends JpaRepository<Interest, Long> {}

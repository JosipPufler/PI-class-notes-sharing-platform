package hr.algebra.pi.repositories;

import hr.algebra.pi.models.Material;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MaterialRepository extends JpaRepository<Material, Long> {
}

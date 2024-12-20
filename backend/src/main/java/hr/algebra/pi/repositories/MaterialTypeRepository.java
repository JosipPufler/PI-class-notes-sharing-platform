package hr.algebra.pi.repositories;

import hr.algebra.pi.models.MaterialType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MaterialTypeRepository extends JpaRepository<MaterialType, Long> {
        Optional<MaterialType> findById(Long id);

}
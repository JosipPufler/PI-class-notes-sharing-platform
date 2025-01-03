package hr.algebra.pi;

import hr.algebra.pi.models.MaterialType;
import hr.algebra.pi.repositories.MaterialTypeRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class MaterialTypeRepositoryTest {

	@Autowired
	private MaterialTypeRepository materialTypeRepository;

	@Test
	public void testFindAll() {
		List<MaterialType> materialTypes = materialTypeRepository.findAll();
		Assertions.assertFalse(materialTypes.isEmpty(), "Podaci bi trebali biti vraćeni");
	}
}

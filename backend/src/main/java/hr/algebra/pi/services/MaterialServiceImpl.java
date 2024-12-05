package hr.algebra.pi.services;

import hr.algebra.pi.models.Material;
import hr.algebra.pi.models.User;
import hr.algebra.pi.repositories.MaterialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class MaterialServiceImpl {

    @Autowired
    private MaterialRepository materialRepository;

    @Autowired
    private UserService userService; // Assuming a service exists for User management

    public Material saveMaterial(Material material) {
        return materialRepository.save(material);
    }

    public List<Material> getAllMaterials() {
        return materialRepository.findAll();
    }

    public Material findById(Long id) {
        return materialRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Material not found"));
    }

    public User findUserById(Long id) {
        return userService.findById(id);
    }
}
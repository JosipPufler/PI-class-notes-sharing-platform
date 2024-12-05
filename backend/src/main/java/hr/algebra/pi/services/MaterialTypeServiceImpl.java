package hr.algebra.pi.services;

import hr.algebra.pi.models.MaterialType;
import hr.algebra.pi.repositories.MaterialTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MaterialTypeServiceImpl {

    @Autowired
    private MaterialTypeRepository materialTypeRepository;


    public MaterialType saveMaterialType(MaterialType materialType) {
        return materialTypeRepository.saveAndFlush(materialType);
    }


    public List<MaterialType> getAllMaterialTypes() {
        return materialTypeRepository.findAll();
    }


    public Optional<MaterialType> findById(Long id) {
        return Optional.ofNullable(materialTypeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("MaterialType not found")));
    }
}
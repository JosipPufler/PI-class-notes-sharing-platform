package hr.algebra.pi.services.material;

import hr.algebra.pi.models.Material;
import hr.algebra.pi.services.MaterialService;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public abstract class MaterialTemplate {

    public final Material saveMaterial(MaterialService materialService, Material material, MultipartFile file) throws IOException {
        prepareMaterial(material);
        processFile(material, file);
        validateMaterial(material);
        return saveMaterial(materialService, material);
    }

    protected abstract void prepareMaterial(Material material);

    protected abstract void processFile(Material material, MultipartFile file) throws IOException;

    protected void validateMaterial(Material material) {
        if (material.getName() == null || material.getName().isEmpty()) {
            throw new IllegalArgumentException("Material must have a name!");
        }
    }

    private Material saveMaterial(MaterialService materialService, Material material) {
        return materialService.saveMaterial(material);
    }
}
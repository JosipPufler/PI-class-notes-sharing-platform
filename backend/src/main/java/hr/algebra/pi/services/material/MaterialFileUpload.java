package hr.algebra.pi.services.material;

import hr.algebra.pi.models.Material;
import hr.algebra.pi.services.utilities.FileStorageFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;

public class MaterialFileUpload extends MaterialTemplate {

    private final FileStorageFactory fileStorageFactory;

    public MaterialFileUpload(FileStorageFactory fileStorageFactory) {
        this.fileStorageFactory = fileStorageFactory;
    }

    @Override
    protected void prepareMaterial(Material material) {
        material.setCreationDate(java.time.LocalDate.now());
    }

    @Override
    protected void processFile(Material material, MultipartFile file) throws IOException {
        Path filePath = fileStorageFactory.storeFile(file);
        material.setLocation(filePath.toString());
    }
}

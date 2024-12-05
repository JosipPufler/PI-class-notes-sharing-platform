package hr.algebra.pi.controllers;
import hr.algebra.pi.models.Material;
import hr.algebra.pi.services.MaterialServiceImpl;
import hr.algebra.pi.services.MaterialTypeServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@RestController
@RequestMapping("/api/materials")
public class MaterialController {

    @Autowired
    private MaterialServiceImpl materialService;

    @Autowired
    private MaterialTypeServiceImpl materialTypeService;

    private final String storageDirectory = "uploads";

    @PostMapping
    public ResponseEntity<Material> createMaterial(
            @RequestParam("file") MultipartFile file,
            @RequestParam("userId") Long userId,
            @RequestParam("materialTypeId") Long materialTypeId,
            @RequestParam("name") String name,
            @RequestParam("description") String description) {

        File directory = new File(storageDirectory);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        try {
            Path filePath = Paths.get(storageDirectory, file.getOriginalFilename());
            Files.write(filePath, file.getBytes());

            Material material = new Material();
            material.setUser(materialService.findUserById(userId));
            material.setMaterialType(materialTypeService.findById(materialTypeId).get());
            material.setName(name);
            material.setDescription(description);
            material.setCreationDate(java.time.LocalDate.now());
            material.setLocation(filePath.toString());

            Material savedMaterial = materialService.saveMaterial(material);

            return ResponseEntity.ok(savedMaterial);
        } catch (IOException e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    @GetMapping
    public ResponseEntity<List<Material>> getAllMaterials() {
        return ResponseEntity.ok(materialService.getAllMaterials());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Material> getMaterialById(@PathVariable Long id) {
        return ResponseEntity.ok(materialService.findById(id));
    }
}
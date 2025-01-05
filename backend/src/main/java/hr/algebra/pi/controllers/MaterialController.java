package hr.algebra.pi.controllers;

import hr.algebra.pi.models.Material;
import hr.algebra.pi.services.MaterialServiceImpl;
import hr.algebra.pi.services.MaterialTypeServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.core.io.Resource;
import org.springframework.core.io.PathResource;
import org.springframework.web.bind.annotation.PathVariable;


import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@RestController
@CrossOrigin("http://127.0.0.1:5500")
@RequestMapping("/api/materials")
public class MaterialController {

    @Autowired
    private MaterialServiceImpl materialService;

    @Autowired
    private MaterialTypeServiceImpl materialTypeService;

    private final String storageDirectory = "uploads";

    @PreAuthorize("isAuthenticated()")
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

    @PreAuthorize("isAuthenticated()")
    @GetMapping
    public ResponseEntity<List<Material>> getAllMaterials() {
        List<Material> materials = materialService.getAllMaterials();
        materials.removeIf(material -> {
            Path filePath = Paths.get(material.getLocation());
            if (!Files.exists(filePath)) {
                materialService.deleteMaterialById(material.getId());
                return true;
            }
            return false;
        });

        return ResponseEntity.ok(materials);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{id}")
    public ResponseEntity<Material> getMaterialById(@PathVariable Long id) {
        return ResponseEntity.ok(materialService.findById(id));
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/download/{filename}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String filename) {
        try {
            Path filePath = Paths.get(storageDirectory, filename);
            Resource resource = new PathResource(filePath);

            if (!resource.exists()) {
                throw new NoSuchFileException("File not found: " + filename);
            }

            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"");
            return ResponseEntity.ok()
                    .headers(headers)
                    .body(resource);
        } catch (NoSuchFileException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }

    @PreAuthorize("isAuthenticated()")
    @PutMapping("/{id}")
    public ResponseEntity<Material> updateMaterialFile(
            @PathVariable Long id,
            @RequestParam("file") MultipartFile file) {

        Material material = materialService.findById(id);
        if (material == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        try {
            Path oldFilePath = Paths.get(material.getLocation());
            if (Files.exists(oldFilePath)) {
                Files.delete(oldFilePath);
            }

            Path newFilePath = Paths.get(storageDirectory, file.getOriginalFilename());
            Files.write(newFilePath, file.getBytes());

            material.setLocation(newFilePath.toString());
            Material updatedMaterial = materialService.saveMaterial(material);

            return ResponseEntity.ok(updatedMaterial);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMaterial(@PathVariable Long id) {
        Material material = materialService.findById(id);

        if (material == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        try {
            Path filePath = Paths.get(material.getLocation());
            if (Files.exists(filePath)) {
                Files.delete(filePath);
            }

            materialService.deleteMaterialById(id);
            return ResponseEntity.noContent().build();
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
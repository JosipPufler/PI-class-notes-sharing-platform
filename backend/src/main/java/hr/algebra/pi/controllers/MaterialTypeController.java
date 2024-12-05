package hr.algebra.pi.controllers;

import hr.algebra.pi.models.MaterialType;
import hr.algebra.pi.services.MaterialTypeServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/material-types")
public class MaterialTypeController {

    @Autowired
    private MaterialTypeServiceImpl materialTypeService;

    @PostMapping
    public ResponseEntity<MaterialType> createMaterialType(@RequestBody MaterialType materialType) {
        return ResponseEntity.ok(materialTypeService.saveMaterialType(materialType));
    }

    @GetMapping
    public ResponseEntity<List<MaterialType>> getAllMaterialTypes() {
//        return ResponseEntity<>.ok(materialTypeService.getAllMaterialTypes());
        return new ResponseEntity<>(materialTypeService.getAllMaterialTypes(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MaterialType> getMaterialTypeById(@PathVariable Long id) {
        return ResponseEntity.ok(materialTypeService.findById(id).get());
    }
}

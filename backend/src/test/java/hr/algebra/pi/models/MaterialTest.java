package hr.algebra.pi.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class MaterialTest {

    private Material material;
    private User user;
    private MaterialType materialType;

    @BeforeEach
    void setUp() {
        material = new Material();
        user = new User();
        materialType = new MaterialType();

        user.setId(1L);
        materialType.setId(1L);
        materialType.setName("Test Type");
    }

    @Test
    void testNewMaterialHasNoValues() {
        Material newMaterial = new Material();
        assertNull(newMaterial.getId());
        assertNull(newMaterial.getUser());
        assertNull(newMaterial.getMaterialType());
        assertNull(newMaterial.getName());
        assertNull(newMaterial.getDescription());
        assertNull(newMaterial.getCreationDate());
        assertNull(newMaterial.getTags());
        assertNull(newMaterial.getLocation());
    }

    @Test
    void testValidMaterialCreation() {
        material.setId(1L);
        material.setUser(user);
        material.setMaterialType(materialType);
        material.setName("Test ime");
        material.setDescription("desc");
        material.setCreationDate(LocalDate.now());
        material.setTags("tagovi,its");
        material.setLocation("uploads/testModelMaterial.pdf");

        assertNotNull(material.getId());
        assertNotNull(material.getUser());
        assertNotNull(material.getMaterialType());
        assertNotNull(material.getName());
        assertNotNull(material.getDescription());
        assertNotNull(material.getCreationDate());
        assertNotNull(material.getTags());
        assertNotNull(material.getLocation());
    }

    @Test
    void testMaterialRelationships() {
        material.setUser(user);
        material.setMaterialType(materialType);

        // Test relationship with User
        assertNotNull(material.getUser());
        assertEquals(1L, material.getUser().getId());

        // Test relationship with MaterialType
        assertNotNull(material.getMaterialType());
        assertEquals(1L, material.getMaterialType().getId());
        assertEquals("Test Type", material.getMaterialType().getName());
    }
} 
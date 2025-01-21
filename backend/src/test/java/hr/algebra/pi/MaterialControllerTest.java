package hr.algebra.pi;

import hr.algebra.pi.controllers.MaterialController;
import hr.algebra.pi.models.Material;
import hr.algebra.pi.models.MaterialType;
import hr.algebra.pi.models.User;
import hr.algebra.pi.services.MaterialService;
import hr.algebra.pi.services.MaterialTypeService;
import hr.algebra.pi.services.Mapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class MaterialControllerTest {

    @Mock
    private MaterialService materialService;

    @Mock
    private MaterialTypeService materialTypeService;

    @Mock
    private Mapper mapper;

    @InjectMocks
    private MaterialController materialController;

    private Material testMaterial;
    private User testUser;
    private MaterialType testMaterialType;
    private MockMultipartFile testFile;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        testUser = new User();
        testUser.setId(1L);

        testMaterialType = new MaterialType();
        testMaterialType.setId(1L);

        testMaterial = new Material();
        testMaterial.setId(1L);
        testMaterial.setName("Test Material");
        testMaterial.setDescription("Test Description");
        testMaterial.setUser(testUser);
        testMaterial.setMaterialType(testMaterialType);
        testMaterial.setLocation("uploads/test.pdf");

        testFile = new MockMultipartFile(
            "file",
            "test.pdf",
            "application/pdf",
            "test content".getBytes()
        );

        when(materialService.findUserById(1L)).thenReturn(testUser);
        when(materialTypeService.findById(1L)).thenReturn(Optional.of(testMaterialType));
    }

    @Test
    void uploadMaterial_Success() {
        when(materialService.saveMaterial(any(Material.class))).thenReturn(testMaterial);

        ResponseEntity<Material> response = materialController.uploadMaterial(
            testFile,
            1L,
            1L,
            "Test Material",
            "Test Description",
            "tag1,tag2"
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Test Material", response.getBody().getName());
    }

    @Test
    void uploadMaterial_Failure() {
        when(materialService.saveMaterial(any(Material.class))).thenThrow(new RuntimeException("Save failed"));

        ResponseEntity<Material> response = materialController.uploadMaterial(
            testFile,
            1L,
            1L,
            "Test Material",
            "Test Description",
            "tag1,tag2"
        );

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void getAllMaterials_Success() {
        List<Material> materials = Arrays.asList(testMaterial);
        when(materialService.getAllMaterials()).thenReturn(materials);

        ResponseEntity<List<Material>> response = materialController.getAllMaterials();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
    }

    @Test
    void getMaterialById_Success() {
        when(materialService.findById(1L)).thenReturn(testMaterial);

        ResponseEntity<Material> response = materialController.getMaterialById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1L, response.getBody().getId());
    }

    @Test
    void deleteMaterial_Success() {
        when(materialService.findById(1L)).thenReturn(testMaterial);

        ResponseEntity<Void> response = materialController.deleteMaterial(1L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(materialService, times(1)).deleteMaterialById(1L);
    }

    @Test
    void deleteMaterial_NotFound() {
        when(materialService.findById(1L)).thenReturn(null);

        ResponseEntity<Void> response = materialController.deleteMaterial(1L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(materialService, never()).deleteMaterialById(1L);
    }

    @Test
    void updateMaterialFile_Success() {
        when(materialService.findById(1L)).thenReturn(testMaterial);
        when(materialService.saveMaterial(any(Material.class))).thenReturn(testMaterial);

        ResponseEntity<Material> response = materialController.updateMaterialFile(1L, testFile);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void updateMaterialFile_NotFound() {
        when(materialService.findById(1L)).thenReturn(null);

        ResponseEntity<Material> response = materialController.updateMaterialFile(1L, testFile);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }
}

package hr.algebra.pi.services;

import hr.algebra.pi.models.Material;
import hr.algebra.pi.models.MaterialType;
import hr.algebra.pi.models.User;
import hr.algebra.pi.repositories.MaterialRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MaterialServiceTest {

    @Mock
    private MaterialRepository materialRepository;

    @Mock
    private UserService userService;

    @InjectMocks
    private MaterialService materialService;

    private Material testMaterial;
    private User testUser;
    private MaterialType testMaterialType;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);

        testMaterialType = new MaterialType();
        testMaterialType.setId(1L);
        testMaterialType.setName("Test Type");

        testMaterial = new Material();
        testMaterial.setId(1L);
        testMaterial.setUser(testUser);
        testMaterial.setMaterialType(testMaterialType);
        testMaterial.setName("Test Material");
        testMaterial.setDescription("Test Description");
        testMaterial.setCreationDate(LocalDate.now());
        testMaterial.setLocation("test/location.pdf");
    }

    @Test
    void saveMaterial_Success() {
        when(materialRepository.save(any(Material.class))).thenReturn(testMaterial);

        Material savedMaterial = materialService.saveMaterial(testMaterial);

        assertNotNull(savedMaterial);
        assertEquals(testMaterial.getId(), savedMaterial.getId());
        assertEquals(testMaterial.getName(), savedMaterial.getName());
        verify(materialRepository, times(1)).save(any(Material.class));
    }

    @Test
    void getAllMaterials_Success() {
        List<Material> materials = Arrays.asList(testMaterial);
        when(materialRepository.findAll()).thenReturn(materials);

        List<Material> result = materialService.getAllMaterials();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(testMaterial.getId(), result.get(0).getId());
        verify(materialRepository, times(1)).findAll();
    }

    @Test
    void findById_Success() {
        when(materialRepository.findById(1L)).thenReturn(Optional.of(testMaterial));

        Material foundMaterial = materialService.findById(1L);

        assertNotNull(foundMaterial);
        assertEquals(testMaterial.getId(), foundMaterial.getId());
        verify(materialRepository, times(1)).findById(1L);
    }

    @Test
    void findById_NotFound() {
        when(materialRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> materialService.findById(999L));
        verify(materialRepository, times(1)).findById(999L);
    }

    @Test
    void findUserById_Success() {
        when(userService.findById(1L)).thenReturn(testUser);

        User foundUser = materialService.findUserById(1L);

        assertNotNull(foundUser);
        assertEquals(testUser.getId(), foundUser.getId());
        verify(userService, times(1)).findById(1L);
    }

    @Test
    void deleteMaterialById_Success() {
        doNothing().when(materialRepository).deleteById(1L);

        materialService.deleteMaterialById(1L);

        verify(materialRepository, times(1)).deleteById(1L);
    }
} 
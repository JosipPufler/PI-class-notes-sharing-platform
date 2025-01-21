package hr.algebra.pi.services;

import hr.algebra.pi.models.MaterialType;
import hr.algebra.pi.repositories.MaterialTypeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MaterialTypeServiceTest {

    @Mock
    private MaterialTypeRepository materialTypeRepository;

    @InjectMocks
    private MaterialTypeService materialTypeService;

    private MaterialType testMaterialType;

    @BeforeEach
    void setUp() {
        testMaterialType = new MaterialType();
        testMaterialType.setId(1L);
        testMaterialType.setName("Test Material Type");
    }

    @Test
    void saveMaterialType_Success() {
        when(materialTypeRepository.saveAndFlush(any(MaterialType.class))).thenReturn(testMaterialType);

        MaterialType savedType = materialTypeService.saveMaterialType(testMaterialType);

        assertNotNull(savedType);
        assertEquals(testMaterialType.getId(), savedType.getId());
        assertEquals(testMaterialType.getName(), savedType.getName());
        verify(materialTypeRepository, times(1)).saveAndFlush(any(MaterialType.class));
    }

    @Test
    void getAllMaterialTypes_Success() {
        List<MaterialType> types = Arrays.asList(testMaterialType);
        when(materialTypeRepository.findAll()).thenReturn(types);

        List<MaterialType> result = materialTypeService.getAllMaterialTypes();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(testMaterialType.getId(), result.get(0).getId());
        assertEquals(testMaterialType.getName(), result.get(0).getName());
        verify(materialTypeRepository, times(1)).findAll();
    }

    @Test
    void findById_Success() {
        when(materialTypeRepository.findById(1L)).thenReturn(Optional.of(testMaterialType));

        Optional<MaterialType> foundType = materialTypeService.findById(1L);

        assertTrue(foundType.isPresent());
        assertEquals(testMaterialType.getId(), foundType.get().getId());
        assertEquals(testMaterialType.getName(), foundType.get().getName());
        verify(materialTypeRepository, times(1)).findById(1L);
    }

    @Test
    void findById_NotFound() {
        when(materialTypeRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> materialTypeService.findById(999L));
        verify(materialTypeRepository, times(1)).findById(999L);
    }

    @Test
    void findById_ReturnsEmptyOptional() {
        when(materialTypeRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> materialTypeService.findById(1L));
        verify(materialTypeRepository, times(1)).findById(1L);
    }
} 
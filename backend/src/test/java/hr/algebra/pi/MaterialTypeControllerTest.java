package hr.algebra.pi;

import com.fasterxml.jackson.databind.ObjectMapper;
import hr.algebra.pi.controllers.MaterialTypeController;
import hr.algebra.pi.models.MaterialType;
import hr.algebra.pi.services.MaterialTypeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MaterialTypeController.class)
public class MaterialTypeControllerTest {
    
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MaterialTypeService materialTypeService;

    @Autowired
    private ObjectMapper objectMapper;

    private MaterialType testMaterialType;

    @BeforeEach
    void setUp() {
        testMaterialType = new MaterialType();
        testMaterialType.setId(1L);
        testMaterialType.setName("Test Material Type");
    }


    @Test
    @WithMockUser
    void getAllMaterialTypes_Success() throws Exception {
        MaterialType secondType = new MaterialType();
        secondType.setId(2L);
        secondType.setName("Second Type");

        when(materialTypeService.getAllMaterialTypes()).thenReturn(Arrays.asList(testMaterialType, secondType));

        mockMvc.perform(get("/api/material-types"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Test Material Type"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].name").value("Second Type"));
    }

    @Test
    void getAllMaterialTypes_Unauthorized() throws Exception {
        mockMvc.perform(get("/api/material-types"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser
    void getMaterialTypeById_Success() throws Exception {
        when(materialTypeService.findById(1L)).thenReturn(Optional.of(testMaterialType));

        mockMvc.perform(get("/api/material-types/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Test Material Type"));
    }

    @Test
    void getMaterialTypeById_Unauthorized() throws Exception {
        mockMvc.perform(get("/api/material-types/1"))
                .andExpect(status().isUnauthorized());
    }
    
}

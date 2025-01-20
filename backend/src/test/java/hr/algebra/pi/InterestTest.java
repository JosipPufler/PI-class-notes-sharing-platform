package hr.algebra.pi;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import hr.algebra.pi.config.SecurityConfiguration;
import hr.algebra.pi.models.DTOs.*;
import hr.algebra.pi.models.Interest;
import hr.algebra.pi.services.InterestService;
import hr.algebra.pi.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestPropertySource("classpath:application-test.properties")
@SpringBootTest
@Import(SecurityConfiguration.class)
@AutoConfigureMockMvc
class InterestTest {
    @Autowired
    public MockMvc mockMvc;

    @Autowired
    TestUtils testUtils;

    @Autowired
    UserService userService;

    @Autowired
    InterestService interestService;

    ObjectMapper objectMapper = new ObjectMapper();

    SignInForm defaultSignInForm = new SignInForm("PPeric", "password", "pperic@gmail.com", "0912233321", "Pero", "Peric");

    InterestCreationForm defaultInterest = new InterestCreationForm("test", null);

    @BeforeEach
    public void setUp() throws Exception {
        userService.deleteAll();
        interestService.deleteAll();
        testUtils.addUser(mockMvc, defaultSignInForm);
    }

    @Test
    void testCreateInterest() throws Exception{
        MvcResult mvcResult = mockMvc.perform(post("/api/interest").header(HttpHeaders.AUTHORIZATION, testUtils
                        .getAuthorizationHeader(mockMvc, defaultSignInForm)).content(testUtils.asJsonString(defaultInterest)).contentType(MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().isCreated()).andReturn();
        String contentAsString = mvcResult.getResponse().getContentAsString();
        Interest interest = objectMapper.readValue(contentAsString, Interest.class);
        assertThat(interest).isNotNull();
        assertThat(interest.getId()).isNotNull();
    }

    @Test
    void testCreateDuplicateInterest() throws Exception{
        Long id = testUtils.createInterest(mockMvc, defaultSignInForm, defaultInterest);
        InterestCreationForm interestCreationForm = new InterestCreationForm(defaultInterest.getName(), id);
        mockMvc.perform(post("/api/interest").header(HttpHeaders.AUTHORIZATION, testUtils.getAuthorizationHeader(mockMvc, defaultSignInForm)).content(testUtils.asJsonString(interestCreationForm)).contentType(MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().isConflict());
    }

    @Test
    void testCreateInterestWithValidParent() throws Exception{
        Long id = testUtils.createInterest(mockMvc, defaultSignInForm, defaultInterest);
        InterestCreationForm interestCreationForm = new InterestCreationForm("test2", id);
        MvcResult mvcResult = mockMvc.perform(post("/api/interest").header(HttpHeaders.AUTHORIZATION, testUtils.getAuthorizationHeader(mockMvc, defaultSignInForm)).content(testUtils.asJsonString(interestCreationForm)).contentType(MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().isCreated()).andReturn();
        String contentAsString = mvcResult.getResponse().getContentAsString();
        Interest interest = objectMapper.readValue(contentAsString, Interest.class);
        assertThat(interest).isNotNull();
        assertThat(interest.parentInterest).isNotNull();
        assertThat(interest.parentInterest.getName()).isEqualTo(defaultInterest.getName());
        assertThat(interest.parentInterest.getId()).isEqualTo(id);
    }

    @Test
    void testCreateInterestWithNonValidParent() throws Exception{
        Long id = testUtils.createInterest(mockMvc, defaultSignInForm, defaultInterest);
        InterestCreationForm interestCreationForm = new InterestCreationForm("test2", id+1);
        mockMvc.perform(post("/api/interest").header(HttpHeaders.AUTHORIZATION, testUtils.getAuthorizationHeader(mockMvc, defaultSignInForm)).content(testUtils.asJsonString(interestCreationForm)).contentType(MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().isNotFound());
    }

    @Test
    void testGetInterests() throws Exception{
        testUtils.createInterest(mockMvc, defaultSignInForm, defaultInterest);
        MvcResult mvcResult = mockMvc.perform(get("/api/interest").header(HttpHeaders.AUTHORIZATION, testUtils.getAuthorizationHeader(mockMvc, defaultSignInForm)))
                .andDo(print()).andExpect(status().isOk()).andReturn();
        String contentAsString = mvcResult.getResponse().getContentAsString();
        List<Interest> interest = objectMapper.readValue(contentAsString, new TypeReference<>() {});
        assertThat(interest).isNotNull().hasSize(1);
    }

    @Test
    void testGetInterest() throws Exception{
        Long id = testUtils.createInterest(mockMvc, defaultSignInForm, defaultInterest);
        MvcResult mvcResult = mockMvc.perform(get("/api/interest/"+id).header(HttpHeaders.AUTHORIZATION, testUtils.getAuthorizationHeader(mockMvc, defaultSignInForm)))
                .andDo(print()).andExpect(status().isOk()).andReturn();
        String contentAsString = mvcResult.getResponse().getContentAsString();
        Interest interest = objectMapper.readValue(contentAsString, Interest.class);
        assertThat(interest).isNotNull();
        assertThat(interest.getName()).isEqualTo(defaultInterest.getName());
    }

    @Test
    void testDeleteInterest() throws Exception{
        Long id = testUtils.createInterest(mockMvc, defaultSignInForm, defaultInterest);
        MvcResult mvcResult = mockMvc.perform(delete("/api/interest/"+id).header(HttpHeaders.AUTHORIZATION, testUtils.getAuthorizationHeader(mockMvc, defaultSignInForm)))
                .andDo(print()).andExpect(status().isOk()).andReturn();
        String contentAsString = mvcResult.getResponse().getContentAsString();
        Boolean response = objectMapper.readValue(contentAsString, Boolean.class);
        assertThat(response).isNotNull().isEqualTo(Boolean.TRUE);
    }

    @Test
    void testUpdateInterest() throws Exception{
        Long id = testUtils.createInterest(mockMvc, defaultSignInForm, defaultInterest);
        Interest updatedInterest = new Interest(id, "test2", null, null);
        MvcResult mvcResult = mockMvc.perform(put("/api/interest").header(HttpHeaders.AUTHORIZATION, testUtils.getAuthorizationHeader(mockMvc, defaultSignInForm))
                        .contentType(MediaType.APPLICATION_JSON).content(testUtils.asJsonString(updatedInterest)))
                .andDo(print()).andExpect(status().isOk()).andReturn();
        String contentAsString = mvcResult.getResponse().getContentAsString();
        Interest response = objectMapper.readValue(contentAsString, Interest.class);
        assertThat(response).isNotNull();
        assertThat(response.getName()).isEqualTo(updatedInterest.getName()).isNotEqualTo(defaultInterest.getName());
    }
}

package hr.algebra.pi;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import hr.algebra.pi.config.SecurityConfiguration;
import hr.algebra.pi.models.DTOs.SignInForm;
import hr.algebra.pi.models.NotificationType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestPropertySource("classpath:application-test.properties")
@SpringBootTest
@Import(SecurityConfiguration.class)
@AutoConfigureMockMvc
public class NotificationTypeTest {
    @Autowired
    MockMvc mockMvc;
    @Autowired
    TestUtils testUtils;

    ObjectMapper objectMapper = new ObjectMapper();

    SignInForm defaultSignInForm = new SignInForm("PPeric", "password", "pperic@gmail.com", "0912233321", "Pero", "Peric");
    Long id;
    @BeforeEach
    public void setUp() throws Exception {
        testUtils.clearData();
        id = testUtils.createNotificationInfoType();
        testUtils.addUser(mockMvc, defaultSignInForm);
    }

    @Test
    void testGetNotificationTypes() throws Exception{
        MvcResult mvcResult = mockMvc.perform(get("/api/notificationType").header(HttpHeaders.AUTHORIZATION, testUtils
                        .getAuthorizationHeader(mockMvc, defaultSignInForm)))
                .andDo(print()).andExpect(status().isOk()).andReturn();
        String contentAsString = mvcResult.getResponse().getContentAsString();
        List<NotificationType> notification = objectMapper.readValue(contentAsString, new TypeReference<>() {});
        assertThat(notification).isNotNull().hasSize(1);
    }

    @Test
    void testGetNotificationTypeById() throws Exception{
        MvcResult mvcResult = mockMvc.perform(get("/api/notificationType/"+id).header(HttpHeaders.AUTHORIZATION, testUtils
                        .getAuthorizationHeader(mockMvc, defaultSignInForm)))
                .andDo(print()).andExpect(status().isOk()).andReturn();
        String contentAsString = mvcResult.getResponse().getContentAsString();
        NotificationType notification = objectMapper.readValue(contentAsString, new TypeReference<>() {});
        assertThat(notification).isNotNull();
        assertThat(notification.getId()).isEqualTo(id);
    }

    @Test
    void testGetNotificationTypeByIncorrectId() throws Exception{
        mockMvc.perform(get("/api/notificationType/"+(id + 1)).header(HttpHeaders.AUTHORIZATION, testUtils
                        .getAuthorizationHeader(mockMvc, defaultSignInForm)))
                .andDo(print()).andExpect(status().isNotFound());
    }
}

package hr.algebra.pi;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import hr.algebra.pi.config.SecurityConfiguration;
import hr.algebra.pi.models.DTOs.InterestCreationForm;
import hr.algebra.pi.models.DTOs.SignInForm;
import hr.algebra.pi.models.Notification;
import hr.algebra.pi.services.NotificationService;
import hr.algebra.pi.services.NotificationTypeService;
import hr.algebra.pi.services.TwoFAService;
import hr.algebra.pi.services.UserService;
import jakarta.persistence.EntityNotFoundException;
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
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestPropertySource("classpath:application-test.properties")
@SpringBootTest
@Import(SecurityConfiguration.class)
@AutoConfigureMockMvc
class NotificationControllerTest {
    @Autowired
    public MockMvc mockMvc;

    @Autowired
    NotificationTypeService notificationTypeService;
    @Autowired
    UserService userService;
    @Autowired
    NotificationService notificationService;
    @Autowired
    TwoFAService twoFAService;

    @Autowired
    TestUtils testUtils;

    ObjectMapper objectMapper = new ObjectMapper();

    SignInForm defaultSignInForm = new SignInForm("PPeric", "password", "pperic@gmail.com", "0912233321", "Pero", "Peric");

    InterestCreationForm defaultInterest = new InterestCreationForm("test", null);

    Long userId;
    Long id;
    Long notificationTypeId;

    @BeforeEach
    public void setUp() throws Exception {
        objectMapper.findAndRegisterModules();
        testUtils.clearData();
        notificationTypeId = testUtils.createNotificationInfoType();
        userId = testUtils.addUser(mockMvc, defaultSignInForm);
        id = createDefaultNotification();
    }

    @Test
    void testGetNotifications() throws Exception{
        MvcResult mvcResult = mockMvc.perform(get("/api/notification/" + userId).header(HttpHeaders.AUTHORIZATION, testUtils
                        .getAuthorizationHeader(mockMvc, defaultSignInForm)))
                .andExpect(status().isOk()).andReturn();
        String contentAsString = mvcResult.getResponse().getContentAsString();
        List<Notification> notification = objectMapper.readValue(contentAsString, new TypeReference<>() {});
        assertThat(notification).isNotNull().hasSize(3);
    }

    @Test
    void testMarkNotificationsAsRead() throws Exception{
        mockMvc.perform(put("/api/notification/" + id).header(HttpHeaders.AUTHORIZATION, testUtils
                        .getAuthorizationHeader(mockMvc, defaultSignInForm)))
                .andExpect(status().isOk());
        assertThat(notificationService.findById(id).getIsRead()).isEqualTo(Boolean.TRUE);
    }

    @Test
    void testDeleteAllUserNotifications() throws Exception{
        mockMvc.perform(delete("/api/notification/clear/" + userId).header(HttpHeaders.AUTHORIZATION, testUtils
                        .getAuthorizationHeader(mockMvc, defaultSignInForm)))
                .andExpect(status().isOk());
        assertThat(notificationService.getAll()).isEmpty();
    }

    @Test
    void testDeleteNotification() throws Exception{
        mockMvc.perform(delete("/api/notification/" + id).header(HttpHeaders.AUTHORIZATION, testUtils
                        .getAuthorizationHeader(mockMvc, defaultSignInForm)))
                .andExpect(status().isOk());
        assertThrows(EntityNotFoundException.class, () -> notificationService.findById(id));
    }

    @Test
    void testFindByNotificationTypeName(){
        assertThrows(EntityNotFoundException.class, () -> notificationTypeService.findByName("test"));
    }

    public Long createDefaultNotification(){
        return notificationService.createInfoNotification("Test", "test", userService.findById(userId)).getId();
    }
}

package hr.algebra.pi;

import com.fasterxml.jackson.databind.ObjectMapper;
import hr.algebra.pi.models.DTOs.*;
import hr.algebra.pi.models.Interest;
import hr.algebra.pi.models.NotificationType;
import hr.algebra.pi.services.NotificationService;
import hr.algebra.pi.services.NotificationTypeService;
import hr.algebra.pi.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Service
public class TestUtils {
    ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    NotificationTypeService notificationTypeService;
    @Autowired
    NotificationService notificationService;
    @Autowired
    UserService userService;

    public String asJsonString(final Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public String getAuthorizationHeader(MockMvc mockMvc, SignInForm signInForm) throws Exception {
        MvcResult mvcResult = mockMvc.perform(post("/api/user/logIn").content(asJsonString(new LogInForm(signInForm.getUsername(), signInForm.getPassword())))
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn();
        String contentAsString = mvcResult.getResponse().getContentAsString();
        LogInResponse logIn = new ObjectMapper().readValue(contentAsString, LogInResponse.class);
        return logIn.getTokenType() + " " + logIn.getToken();
    }

    public long addUser(MockMvc mockMvc, SignInForm user) throws Exception {
        MvcResult mvcResult = mockMvc.perform(post("/api/user/signIn").content(asJsonString(user)).contentType(MediaType.APPLICATION_JSON)).andReturn();
        String contentAsString = mvcResult.getResponse().getContentAsString();
        UserDTO userDto = new ObjectMapper().readValue(contentAsString, UserDTO.class);
        return userDto.getId();
    }

    public Long createInterest(MockMvc mockMvc, SignInForm signInForm, InterestCreationForm interestCreationForm) throws Exception {
        MvcResult mvcResult = mockMvc.perform(post("/api/interest").header(HttpHeaders.AUTHORIZATION, getAuthorizationHeader(mockMvc, signInForm)).content(asJsonString(interestCreationForm)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated()).andReturn();
        String contentAsString = mvcResult.getResponse().getContentAsString();
        Interest interest = objectMapper.readValue(contentAsString, Interest.class);
        return interest.getId();
    }

    public Long createNotificationInfoType(){
        return notificationTypeService.create(new NotificationType(null, "Info", null)).getId();
    }

    public void clearData() {
        userService.deleteAll();
        notificationService.deleteAll();
        notificationTypeService.deleteAll();
    }
}

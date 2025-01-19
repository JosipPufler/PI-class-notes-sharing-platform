package hr.algebra.pi;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import hr.algebra.pi.config.SecurityConfiguration;
import hr.algebra.pi.models.DTOs.*;
import hr.algebra.pi.models.TwoFactorAuthenticationEntry;
import hr.algebra.pi.models.User;
import hr.algebra.pi.models.UserSettings;
import hr.algebra.pi.services.TwoFAService;
import hr.algebra.pi.services.UserDtoAdapter;
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
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@TestPropertySource("classpath:application-test.properties")
@SpringBootTest
@Import(SecurityConfiguration.class)
@AutoConfigureMockMvc
class UserTest {
    @Autowired
    public MockMvc mockMvc;

    @Autowired
    public TestUtils testUtils;
    @Autowired
    public WebApplicationContext webApplicationContext;

    @Autowired
    UserService userService;

    ObjectMapper objectMapper = new ObjectMapper();

    SignInForm defaultSignInForm = new SignInForm("PPeric", "password", "pperic@gmail.com", "0912233321", "Pero", "Peric");
    @Autowired
    private TwoFAService twoFAService;

    @BeforeEach
    public void setUp(){
        testUtils.clearData();
    }

    @Test
    void testSignIn() throws Exception {
        MvcResult mvcResult = mockMvc.perform(post("/api/user/signIn").content(testUtils.asJsonString(defaultSignInForm)).contentType(MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().isCreated()).andReturn();
        String contentAsString = mvcResult.getResponse().getContentAsString();
        UserDTO userDto = objectMapper.readValue(contentAsString, UserDTO.class);
        assertThat(userDto).isNotNull();
        assertThat(userDto.getId()).isNotNull();
    }

    @Test
    void testCreateUserWithInterests() throws Exception {
        testUtils.addUser(mockMvc, defaultSignInForm);
        SignInForm updatedUserForm = new SignInForm("Pero2", defaultSignInForm.getPassword(), "pero2@gmail.com", "0915544879", defaultSignInForm.getFirstName(), defaultSignInForm.getLastName());

        Long interestId = testUtils.createInterest(mockMvc, defaultSignInForm, new InterestCreationForm("test", null));
        updatedUserForm.setInterests(List.of(interestId));
        MvcResult mvcResult = mockMvc.perform(post("/api/user/signIn").content(testUtils.asJsonString(updatedUserForm)).contentType(MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().isCreated()).andReturn();
        String contentAsString = mvcResult.getResponse().getContentAsString();
        UserDTO userDto = objectMapper.readValue(contentAsString, UserDTO.class);
        assertThat(userDto).isNotNull();
        assertThat(userDto.getId()).isNotNull();
    }

    @Test
    void testLogIn() throws Exception{
        testUtils.addUser(mockMvc, defaultSignInForm);

        MvcResult mvcResult = mockMvc.perform(post("/api/user/logIn").content(testUtils.asJsonString(new LogInForm(defaultSignInForm.getUsername(), defaultSignInForm.getPassword())))
                        .contentType(MediaType.APPLICATION_JSON))
                        .andDo(print()).andExpect(status().isOk()).andReturn();
        String contentAsString = mvcResult.getResponse().getContentAsString();
        LogInResponse logInResponse = objectMapper.readValue(contentAsString, LogInResponse.class);
        assertThat(logInResponse.getToken()).isNotNull().isNotBlank().isNotEmpty();
        assertThat(logInResponse.getUsername()).isEqualTo(defaultSignInForm.getUsername());
    }

    @Test
    void testLogInWithDeactivatedUser() throws Exception{
        long id = testUtils.addUser(mockMvc, defaultSignInForm);

        mockMvc.perform(delete("/api/user/"+id).header(HttpHeaders.AUTHORIZATION, testUtils.getAuthorizationHeader(mockMvc, defaultSignInForm))).andExpect(status().isOk());

        mockMvc.perform(post("/api/user/logIn").content(testUtils.asJsonString(new LogInForm(defaultSignInForm.getUsername(), defaultSignInForm.getPassword())))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().isUnauthorized());
    }

    @Test
    void test2FALogIn() throws Exception{
        testUtils.createNotificationInfoType();
        long userId = testUtils.addUser(mockMvc, defaultSignInForm);
        UserSettings userSettings = new UserSettings();
        userSettings.setTwoFactorAuthenticationEnabled(true);
        mockMvc.perform(put("/api/user/updateSettings/" + userId).header(HttpHeaders.AUTHORIZATION, testUtils.getAuthorizationHeader(mockMvc, defaultSignInForm)).content(testUtils.asJsonString(userSettings))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        mockMvc.perform(post("/api/user/logIn").content(testUtils.asJsonString(new LogInForm(defaultSignInForm.getUsername(), defaultSignInForm.getPassword())))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
        TwoFactorAuthenticationEntry code = twoFAService.getAll().getFirst();
        LogInFormWithAuthentication logInFormWithAuthentication = new LogInFormWithAuthentication();
        logInFormWithAuthentication.setUsername(defaultSignInForm.getUsername());
        logInFormWithAuthentication.setPassword(defaultSignInForm.getPassword());
        logInFormWithAuthentication.setCode(code.getCode());

        MvcResult mvcResult = mockMvc.perform(post("/api/user/confirmLogIn").content(testUtils.asJsonString(logInFormWithAuthentication)).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andReturn();
        String contentAsString = mvcResult.getResponse().getContentAsString();
        LogInResponse logInResponse = objectMapper.readValue(contentAsString, LogInResponse.class);
        assertThat(logInResponse.getToken()).isNotNull().isNotBlank().isNotEmpty();
        assertThat(logInResponse.getUsername()).isEqualTo(defaultSignInForm.getUsername());
    }

    @Test
    void test2FALogInWithoutCode() throws Exception{
        testUtils.createNotificationInfoType();
        long userId = testUtils.addUser(mockMvc, defaultSignInForm);
        UserSettings userSettings = new UserSettings();
        userSettings.setTwoFactorAuthenticationEnabled(true);
        mockMvc.perform(put("/api/user/updateSettings/" + userId).header(HttpHeaders.AUTHORIZATION, testUtils.getAuthorizationHeader(mockMvc, defaultSignInForm)).content(testUtils.asJsonString(userSettings))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        LogInFormWithAuthentication logInFormWithAuthentication = new LogInFormWithAuthentication();
        logInFormWithAuthentication.setUsername(defaultSignInForm.getUsername());
        logInFormWithAuthentication.setPassword(defaultSignInForm.getPassword());
        logInFormWithAuthentication.setCode(null);

        mockMvc.perform(post("/api/user/confirmLogIn").content(testUtils.asJsonString(logInFormWithAuthentication)).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isUnauthorized());
    }

    @Test
    void testUpdateUser() throws Exception{
        long id = testUtils.addUser(mockMvc, defaultSignInForm);
        SignInForm updatedUserForm = new SignInForm("Pero2", defaultSignInForm.getPassword(), defaultSignInForm.getEmail(), defaultSignInForm.getPhoneNumber(), defaultSignInForm.getFirstName(), defaultSignInForm.getLastName());

        MvcResult mvcResult = mockMvc.perform(put("/api/user/"+id).header(HttpHeaders.AUTHORIZATION, testUtils.getAuthorizationHeader(mockMvc, defaultSignInForm)).content(testUtils.asJsonString(updatedUserForm))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().isOk()).andReturn();
        String contentAsString = mvcResult.getResponse().getContentAsString();
        assertThat(contentAsString).isEqualTo("\"Success\"");
    }

    @Test
    void testUpdateUserWithoutPassword() throws Exception{
        long id = testUtils.addUser(mockMvc, defaultSignInForm);
        SignInForm updatedUserForm = new SignInForm("Pero2", null, defaultSignInForm.getEmail(), defaultSignInForm.getPhoneNumber(), defaultSignInForm.getFirstName(), defaultSignInForm.getLastName());

        MvcResult mvcResult = mockMvc.perform(put("/api/user/"+id).header(HttpHeaders.AUTHORIZATION, testUtils.getAuthorizationHeader(mockMvc, defaultSignInForm)).content(testUtils.asJsonString(updatedUserForm))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().isOk()).andReturn();
        String contentAsString = mvcResult.getResponse().getContentAsString();
        assertThat(contentAsString).isEqualTo("\"Success\"");
    }

    @Test
    void testUpdateUserSettings() throws Exception{
        long id = testUtils.addUser(mockMvc, defaultSignInForm);
        UserSettings userSettings = new UserSettings();
        userSettings.setTwoFactorAuthenticationEnabled(true);
        MvcResult mvcResult = mockMvc.perform(put("/api/user/updateSettings/"+id).header(HttpHeaders.AUTHORIZATION, testUtils.getAuthorizationHeader(mockMvc, defaultSignInForm)).content(testUtils.asJsonString(userSettings))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().isOk()).andReturn();
        String contentAsString = mvcResult.getResponse().getContentAsString();
        Boolean returnMessage = objectMapper.readValue(contentAsString, Boolean.class);
        assertThat(returnMessage).isEqualTo(Boolean.TRUE);
    }

    @Test
    void testDeleteUser() throws Exception{
        long id = testUtils.addUser(mockMvc, defaultSignInForm);
        MvcResult mvcResult = mockMvc.perform(delete("/api/user/clear/"+id)
                        .header(HttpHeaders.AUTHORIZATION, testUtils.getAuthorizationHeader(mockMvc, defaultSignInForm)))
                .andDo(print()).andExpect(status().isOk()).andReturn();
        String contentAsString = mvcResult.getResponse().getContentAsString();
        Boolean returnMessage = objectMapper.readValue(contentAsString, Boolean.class);
        assertThat(returnMessage).isEqualTo(Boolean.TRUE);
    }

    @Test
    void testGetAllUsers() throws Exception{
        testUtils.addUser(mockMvc, defaultSignInForm);
        MvcResult mvcResult = mockMvc.perform(get("/api/user").header(HttpHeaders.AUTHORIZATION, testUtils.getAuthorizationHeader(mockMvc, defaultSignInForm)))
                .andDo(print()).andExpect(status().isOk()).andReturn();
        String contentAsString = mvcResult.getResponse().getContentAsString();
        List<UserDTO> users = objectMapper.readValue(contentAsString, new TypeReference<>() {});
        assertThat(users).hasSize(1);
    }

    @Test
    void testGetOneUser() throws Exception{
        long id = testUtils.addUser(mockMvc, defaultSignInForm);
        MvcResult mvcResult = mockMvc.perform(get("/api/user/" + id)
                        .header(HttpHeaders.AUTHORIZATION, testUtils.getAuthorizationHeader(mockMvc, defaultSignInForm)))
                        .andDo(print()).andExpect(status().isOk()).andReturn();
        String contentAsString = mvcResult.getResponse().getContentAsString();
        UserDTO users = objectMapper.readValue(contentAsString, UserDTO.class);
        assertThat(users.getUsername()).isEqualTo(defaultSignInForm.getUsername());
    }

    @Test
    void testDeactivateUser() throws Exception{
        long id = testUtils.addUser(mockMvc, defaultSignInForm);
        MvcResult mvcResult = mockMvc.perform(delete("/api/user/" + id)
                        .header(HttpHeaders.AUTHORIZATION, testUtils.getAuthorizationHeader(mockMvc, defaultSignInForm)))
                .andDo(print()).andExpect(status().isOk()).andReturn();
        String contentAsString = mvcResult.getResponse().getContentAsString();
        Boolean success = objectMapper.readValue(contentAsString, Boolean.class);
        assertThat(success).isEqualTo(Boolean.TRUE);
    }

    @Test
    void testDeactivateNonExistingUser() throws Exception{
        long id = testUtils.addUser(mockMvc, defaultSignInForm);
        MvcResult mvcResult = mockMvc.perform(delete("/api/user/" + (id-1))
                        .header(HttpHeaders.AUTHORIZATION, testUtils.getAuthorizationHeader(mockMvc, defaultSignInForm)))
                .andDo(print()).andExpect(status().isNotFound()).andReturn();
        String contentAsString = mvcResult.getResponse().getContentAsString();
        Boolean success = objectMapper.readValue(contentAsString, Boolean.class);
        assertThat(success).isEqualTo(Boolean.FALSE);
    }

    @Test
    void testDuplicateValueValidation() throws Exception{
        testUtils.addUser(mockMvc, defaultSignInForm);
        mockMvc.perform(post("/api/user/signIn").content(testUtils.asJsonString(defaultSignInForm)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict());
    }

    @Test
    void test2FAServiceMethodsUsingUserId() throws Exception{
        long id = testUtils.addUser(mockMvc, defaultSignInForm);
        twoFAService.create(new TwoFactorAuthenticationEntry(null, "1234", LocalDateTime.now(), userService.findById(id)));
        twoFAService.deleteAllUserEntries(id+1);
        assertThat(twoFAService.getByUserId(id+1)).isNotPresent();
        twoFAService.deleteAllUserEntries(id);
    }

    @Test
    void testUserDtoAdapter() {
        User user = new User(1L, "User", "password", "firstname", "lastname", "email", "phone", true, null, null, null, null);
        UserDTO userDTO = new UserDtoAdapter(user);
        assertThat(userDTO.getUsername()).isEqualTo(user.getUsername());
        assertThat(userDTO.getFirstName()).isEqualTo(user.getFirstName());
        assertThat(userDTO.getLastName()).isEqualTo(user.getLastName());
        assertThat(userDTO.getId()).isEqualTo(user.getId());
        assertThat(userDTO.getActive()).isEqualTo(user.isActive());
        assertThat(userDTO.getEmail()).isEqualTo(user.getEmail());
        assertThat(userDTO.getPhoneNumber()).isEqualTo(user.getPhoneNumber());

        User newUser = new User(2L, "User2", "password2", "firstname2", "lastname2", "email2", "phone2", false, null, null, null, null);
        UserDTO newUserDto = new UserDtoAdapter(user);
        newUserDto.setSettings(null);
        assertThat(newUserDto.getSettings()).isEqualTo(new UserSettings());

        newUserDto.setSettings(new UserSettings());
        assertThat(newUserDto.getSettings()).isEqualTo(new UserSettings());

        newUserDto.setId(newUser.getId());
        newUserDto.setFirstName(newUser.getFirstName());
        newUserDto.setLastName(newUser.getLastName());
        newUserDto.setUsername(newUser.getUsername());
        newUserDto.setPhoneNumber(newUser.getPhoneNumber());
        newUserDto.setEmail(newUser.getEmail());
        newUserDto.setInterests(new ArrayList<>());
        newUserDto.setActive(newUser.isActive());

        assertThat(newUserDto.getUsername()).isEqualTo(newUser.getUsername());
        assertThat(newUserDto.getFirstName()).isEqualTo(newUser.getFirstName());
        assertThat(newUserDto.getLastName()).isEqualTo(newUser.getLastName());
        assertThat(newUserDto.getId()).isEqualTo(newUser.getId());
        assertThat(newUserDto.getActive()).isEqualTo(newUser.isActive());
        assertThat(newUserDto.getEmail()).isEqualTo(newUser.getEmail());
        assertThat(newUserDto.getPhoneNumber()).isEqualTo(newUser.getPhoneNumber());
    }
}

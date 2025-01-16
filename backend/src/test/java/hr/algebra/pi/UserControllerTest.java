package hr.algebra.pi;

import com.fasterxml.jackson.databind.ObjectMapper;
import hr.algebra.pi.models.DTOs.SignInForm;
import hr.algebra.pi.models.DTOs.UserDTO;
import hr.algebra.pi.services.Mapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class UserControllerTest{
    @Autowired
    MockMvc mockMvc;
    @Autowired
    Mapper mapper;

    SignInForm defaultSignInForm = new SignInForm("PPeric", "password", "pperic@gmail.com", "0912233321", "Pero", "Peric");

    @Test
    void testSignIn() throws Exception {
        MvcResult mvcResult = mockMvc.perform(post("/api/user/signIn").content(asJsonString(defaultSignInForm)).contentType(MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().isCreated()).andReturn();
        String contentAsString = mvcResult.getResponse().getContentAsString();
        UserDTO productResponseDTO = new ObjectMapper().readValue(contentAsString, UserDTO.class);
        assertThat(productResponseDTO).isNotNull();
    }

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /*MockMvc mockMvc;
    MockMvcRequestBuilder mockMvcRequestBuilder = ;

    @BeforeEach
    public void setup(){
        createDefaultUser();
    }

    private void createDefaultUser() {

    }

    @Test
    void getAllUsers() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/").headers(HttpHeaders.AUTHORIZATION, ));
    }*/

    /*@Mock
    private UserService userService;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private Mapper mapper;
    @Mock
    private JwtService jwtService;
    @Mock
    MailService mailService;
    @Mock
    TwoFAService twoFAService;
    @InjectMocks
    private UserController userController;



    @Test
    public void testCreateUser() {
        userController.addUser(defaultSignInForm);
        assertTrue(userController.getUsers().hasBody() && Objects.requireNonNull(userController.getUsers().getBody()).size() == 1);
    }

    @Test
    public void testDeleteUser() {
        Long userId = Objects.requireNonNull(userController.addUser(defaultSignInForm).getBody()).getId();
        ResponseEntity<Boolean> booleanResponseEntity = userController.deleteUser(userId);
        assertTrue(userController.getUsers().hasBody()
                && !Objects.requireNonNull(userController.getUser(userId).getBody()).getActive()
                && Objects.requireNonNull(booleanResponseEntity.getBody()));
    }

    @Test
    public void testUpdateUser() {
        String newUsername = "Pero2";
        String newPassword = "password2";
        String newFirstName = "Mirko";
        Long userId = Objects.requireNonNull(userController.addUser(defaultSignInForm).getBody()).getId();
        SignInForm updatedUserData = defaultSignInForm;
        updatedUserData.setPassword(newPassword);
        updatedUserData.setUsername(newUsername);
        updatedUserData.setFirstName(newFirstName);
        userController.updateUser(userId, defaultSignInForm);
        UserDTO user = Objects.requireNonNull(userController.getUser(userId).getBody());
        assertTrue(user.getUsername().equals(newUsername) && user.getFirstName().equals(newFirstName));
    }

    @Test
    public void testLoginUser(){
        userController.addUser(defaultSignInForm);
        ResponseEntity<LogInResponse> login = userController.login(new LogInForm(defaultSignInForm.getUsername(), defaultSignInForm.getPassword()));
        assertTrue(login.hasBody() && Objects.equals(Objects.requireNonNull(login.getBody()).getUsername(), defaultSignInForm.getUsername()));
    }*/
}

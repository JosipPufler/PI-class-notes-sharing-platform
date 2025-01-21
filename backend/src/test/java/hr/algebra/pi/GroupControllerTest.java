package hr.algebra.pi;

import hr.algebra.pi.controllers.GroupController;
import hr.algebra.pi.models.CreateGroupForm;
import hr.algebra.pi.models.Group;
import hr.algebra.pi.models.User;
import hr.algebra.pi.services.GroupService;
import hr.algebra.pi.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Date;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class GroupControllerTest {
    private MockMvc mockMvc;

    @Mock
    private GroupService groupService;

    @Mock
    private UserService userService;

    @InjectMocks
    private GroupController groupController;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        SecurityContextHolder.setContext(securityContext);
        mockMvc = MockMvcBuilders.standaloneSetup(groupController).build();
    }

    @Test
    void testGetGroups() {
        List<Group> groups = List.of(new Group());
        when(groupService.getAllGroups()).thenReturn(groups);

        ResponseEntity<List<Group>> response = groupController.getGroups();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(groups, response.getBody());
    }

    @Test
    void testAddGroup() {
        CreateGroupForm form = new CreateGroupForm("Test Group", "Description");
        Group group = new Group(form);
        User user = new User();
        when(groupService.createGroup(any(Group.class))).thenReturn(group);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("username");
        when(userService.findByUsername("username")).thenReturn(user);

        ResponseEntity<Group> response = groupController.addGroup(form);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(group, response.getBody());
        verify(groupService, times(1)).addUserToGroup(group, user);
    }

    @Test
    void testDataIntegrityViolationException() throws Exception {
        CreateGroupForm form = new CreateGroupForm("Test Group", "Description");
        doThrow(new DataIntegrityViolationException("groups_name_key")).when(groupService).createGroup(any(Group.class));

        mockMvc.perform(post("/api/group")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Test Group\",\"description\":\"Description\"}"))
                .andExpect(status().isConflict())
                .andExpect(content().string("\"name\""));
    }

    @Test
    void testGetGroupById() {
        Group group = new Group();
        when(groupService.getGroupById(1L)).thenReturn(group);

        ResponseEntity<Group> response = groupController.getGroupById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(group, response.getBody());
    }

    @Test
    void testGetGroupById_Exception() {
        when(groupService.getGroupById(1L)).thenThrow(new RuntimeException("Unexpected error"));

        ResponseEntity<Group> response = groupController.getGroupById(1L);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    void testAddUserToGroup() {
        Group group = new Group();
        User user = new User();
        when(groupService.getGroupById(1L)).thenReturn(group);
        when(userService.findById(1L)).thenReturn(user);
        when(groupService.addUserToGroup(group, user)).thenReturn(true);

        ResponseEntity<String> response = groupController.addUserToGroup(1L, Map.of("userId", 1L));

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("User added to group", response.getBody());
    }

    @Test
    void testAddUserToGroup_GroupNotFound() {
        when(groupService.getGroupById(1L)).thenReturn(null);

        ResponseEntity<String> response = groupController.addUserToGroup(1L, Map.of("userId", 1L));

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Group or User not found", response.getBody());
    }

    @Test
    void testAddUserToGroup_UserNotFound() {
        Group group = new Group();
        when(groupService.getGroupById(1L)).thenReturn(group);
        when(userService.findById(1L)).thenReturn(null);

        ResponseEntity<String> response = groupController.addUserToGroup(1L, Map.of("userId", 1L));

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Group or User not found", response.getBody());
    }

    @Test
    void testAddUserToGroup_UserAlreadyInGroup() {
        Group group = new Group();
        User user = new User();
        group.getUsers().add(user);
        when(groupService.getGroupById(1L)).thenReturn(group);
        when(userService.findById(1L)).thenReturn(user);
        when(groupService.addUserToGroup(group, user)).thenReturn(false);

        ResponseEntity<String> response = groupController.addUserToGroup(1L, Map.of("userId", 1L));

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("User is already in the group", response.getBody());
    }

    @Test
    void testGetGroupsForUser_UserNotFound() {
        when(userService.findById(1L)).thenReturn(null);

        ResponseEntity<List<Group>> response = groupController.getGroupsForUser(1L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testGetGroupsForUser_UserExists() {
        User user = new User();
        user.setId(1L);
        List<Group> groups = List.of(new Group(), new Group());
        when(userService.findById(1L)).thenReturn(user);
        when(groupService.getGroupsForUser(user)).thenReturn(groups);

        ResponseEntity<List<Group>> response = groupController.getGroupsForUser(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(groups, response.getBody());
        verify(userService, times(1)).findById(1L);
        verify(groupService, times(1)).getGroupsForUser(user);
    }

    @Test
    void testDeleteGroup_GroupExists() {
        Group group = new Group();
        when(groupService.getGroupById(1L)).thenReturn(group);

        ResponseEntity<String> response = groupController.deleteGroup(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Group deleted successfully", response.getBody());
        verify(groupService, times(1)).deleteGroup(group);
    }

    @Test
    void testDeleteGroup_GroupNotFound() {
        when(groupService.getGroupById(1L)).thenReturn(null);

        ResponseEntity<String> response = groupController.deleteGroup(1L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Group not found", response.getBody());
    }

    @Test
    void testGeneralDataIntegrityViolationException() {
        DataIntegrityViolationException exception = new DataIntegrityViolationException("general error");
        ResponseEntity<String> response = groupController.dataIntegrityViolantionException(exception);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("\"general\"", response.getBody());
    }

    @Test
    void testGroupToString() {
        Group group = new Group();
        group.setId(1L);
        group.setName("Test Group");
        group.setDescription("Test Description");
        group.setDateCreation(new Date());

        String expected = "Group{id=1, name='Test Group', description='Test Description', dateCreation=" + group.getDateCreation() + "}";
        String actual = group.toString();

        assertEquals(expected, actual);
    }
}
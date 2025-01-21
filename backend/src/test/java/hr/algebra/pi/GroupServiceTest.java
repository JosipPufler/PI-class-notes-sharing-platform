package hr.algebra.pi;

import hr.algebra.pi.models.CreateGroupForm;
import hr.algebra.pi.models.Group;
import hr.algebra.pi.models.User;
import hr.algebra.pi.repositories.GroupRepo;
import hr.algebra.pi.repositories.UserRepo;
import hr.algebra.pi.services.GroupService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GroupServiceTest {

    @Mock
    private GroupRepo groupRepo;

    @Mock
    private UserRepo userRepo;

    @InjectMocks
    private GroupService groupService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateGroup() {
        Group group = new Group();
        when(groupRepo.saveAndFlush(group)).thenReturn(group);

        Group createdGroup = groupService.createGroup(group);

        assertNotNull(createdGroup);
        verify(groupRepo, times(1)).saveAndFlush(group);
    }

    @Test
    void testCreateGroupFromForm() {
        CreateGroupForm form = new CreateGroupForm("Test Group", "Test Description");
        Group group = new Group(form);
        when(groupRepo.saveAndFlush(group)).thenReturn(group);

        Group createdGroup = groupService.createGroup(group);

        assertNotNull(createdGroup);
        assertEquals("Test Group", createdGroup.getName());
        assertEquals("Test Description", createdGroup.getDescription());
        verify(groupRepo, times(1)).saveAndFlush(group);
    }

    @Test
    void testGetAllGroups() {
        List<Group> groups = new ArrayList<>();
        when(groupRepo.findAll()).thenReturn(groups);

        List<Group> result = groupService.getAllGroups();

        assertEquals(groups, result);
        verify(groupRepo, times(1)).findAll();
    }

    @Test
    void testGetGroupById() {
        Group group = new Group();
        when(groupRepo.findById(1L)).thenReturn(Optional.of(group));

        Group result = groupService.getGroupById(1L);

        assertEquals(group, result);
        verify(groupRepo, times(1)).findById(1L);
    }

    @Test
    void testAddUserToGroup_UserAlreadyInGroup() {
        Group group = new Group();
        User user = new User();
        group.getUsers().add(user);

        boolean result = groupService.addUserToGroup(group, user);

        assertFalse(result);
        verify(groupRepo, never()).saveAndFlush(group);
    }

    @Test
    void testAddUserToGroup_UserNotInGroup() {
        Group group = new Group();
        User user = new User();

        boolean result = groupService.addUserToGroup(group, user);

        assertTrue(result);
        assertTrue(group.getUsers().contains(user));
        verify(groupRepo, times(1)).saveAndFlush(group);
    }

    @Test
    void testDeleteGroup() {
        Group group = new Group();
        groupService.deleteGroup(group);

        verify(groupRepo, times(1)).delete(group);
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
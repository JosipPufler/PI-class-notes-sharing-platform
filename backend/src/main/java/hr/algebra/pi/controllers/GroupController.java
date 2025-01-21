package hr.algebra.pi.controllers;

import hr.algebra.pi.models.CreateGroupForm;
import hr.algebra.pi.models.Group;
import hr.algebra.pi.models.User;
import hr.algebra.pi.repositories.GroupRepo;
import hr.algebra.pi.repositories.UserRepo;
import hr.algebra.pi.services.GroupFactory;
import hr.algebra.pi.services.GroupService;
import hr.algebra.pi.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping(path="api/group")
public class GroupController {
    private final GroupService groupService;
    private final UserService userService;

    @Autowired
    public GroupController(GroupService groupService, UserService userService) {
        this.groupService = groupService;
        this.userService = userService;
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping
    public ResponseEntity<List<Group>> getGroups() {
        return new ResponseEntity<>(groupService.getAllGroups(), HttpStatus.OK);
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping
    public ResponseEntity<Group> addGroup(@RequestBody CreateGroupForm createGroupForm) {
        Group group = GroupFactory.createGroup(createGroupForm);
        Group newGroup = groupService.createGroup(group);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userService.findByUsername(username);

        groupService.addUserToGroup(newGroup, user);

        return new ResponseEntity<>(newGroup, HttpStatus.CREATED);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<String> dataIntegrityViolantionException(final DataIntegrityViolationException e) {
        String mostSpecificCauseMessage = e.getMostSpecificCause().getMessage();
        if (e.getMessage().toLowerCase().contains("groups_name_key")){
            return new ResponseEntity<>("\"name\"", HttpStatus.CONFLICT);
        }
        return new ResponseEntity<>("\"general\"", HttpStatus.CONFLICT);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Group> getGroupById(@PathVariable Long id) {
        try {
            Group group = groupService.getGroupById(id);
            if (group != null) {
                return ResponseEntity.ok(group);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/{groupId}/addUser")
    public ResponseEntity<String> addUserToGroup(@PathVariable Long groupId, @RequestBody Map<String, Long> request) {
        Long userId = request.get("userId");
        Group group = groupService.getGroupById(groupId);
        User user = userService.findById(userId);

        if (group != null && user != null) {
            boolean added = groupService.addUserToGroup(group, user);
            if (added) {
                return new ResponseEntity<>("User added to group", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("User is already in the group", HttpStatus.CONFLICT);
            }
        } else {
            return new ResponseEntity<>("Group or User not found", HttpStatus.NOT_FOUND);
        }
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Group>> getGroupsForUser(@PathVariable Long userId) {
        User user = userService.findById(userId);
        if (user != null) {
            List<Group> groups = groupService.getGroupsForUser(user);
            return new ResponseEntity<>(groups, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteGroup(@PathVariable Long id) {
        Group group = groupService.getGroupById(id);
        if (group != null) {
            groupService.deleteGroup(group);
            return new ResponseEntity<>("Group deleted successfully", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Group not found", HttpStatus.NOT_FOUND);
        }
    }

}

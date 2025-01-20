package hr.algebra.pi.controllers;

import hr.algebra.pi.models.CreateGroupForm;
import hr.algebra.pi.models.Group;
import hr.algebra.pi.models.User;
import hr.algebra.pi.services.GroupService;
import hr.algebra.pi.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
        Group group = new Group(createGroupForm);
        Group newGroup = groupService.createGroup(group);
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

}

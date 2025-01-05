package hr.algebra.pi.controllers;

import hr.algebra.pi.models.CreateGroupForm;
import hr.algebra.pi.models.Group;
import hr.algebra.pi.services.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping(path="api/group")
public class GroupController {
    private final GroupService groupService;

    @Autowired
    public GroupController(GroupService groupService) {
        this.groupService = groupService;
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
        Group group = groupService.getGroupById(id);
        if (group != null) {
            return ResponseEntity.ok(group);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}

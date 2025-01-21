package hr.algebra.pi.services;

import hr.algebra.pi.models.Group;
import hr.algebra.pi.models.User;
import hr.algebra.pi.repositories.GroupRepo;
import hr.algebra.pi.repositories.UserRepo;
import jakarta.transaction.Transactional;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Scope("singleton")
@Transactional
public class GroupService {
    private static GroupService instance;
    private final GroupRepo groupRepo;
    private final UserRepo userRepo;

    public GroupService(GroupRepo groupRepo, UserRepo userRepo) {
        this.groupRepo = groupRepo;
        this.userRepo = userRepo;
    }

    public Group createGroup(Group group) {
        return groupRepo.saveAndFlush(group);
    }

    public List<Group> getAllGroups() {
        return groupRepo.findAll();
    }

    public Group getGroupById(Long id) {
        return groupRepo.findById(id).orElse(null);
    }

    public boolean addUserToGroup(Group group, User user) {
        if (group.getUsers().contains(user)) {
            return false;
        } else {
            group.getUsers().add(user);
            groupRepo.saveAndFlush(group);
            return true;
        }
    }

    public List<Group> getGroupsForUser(User user) {
        return groupRepo.findByUsersContaining(user);
    }

    public void deleteGroup(Group group) {
        group.getUsers().clear();
        groupRepo.delete(group);
    }
}

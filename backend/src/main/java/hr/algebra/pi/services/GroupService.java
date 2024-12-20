package hr.algebra.pi.services;

import hr.algebra.pi.models.Group;
import hr.algebra.pi.repositories.GroupRepo;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Transactional
public class GroupService {
    private final GroupRepo groupRepo;

    public GroupService(GroupRepo groupRepo) {
        this.groupRepo = groupRepo;
    }

    public Group createGroup(Group group) {
        return groupRepo.saveAndFlush(group);
    }

    public List<Group> getAllGroups() {
        return groupRepo.findAll();
    }
}

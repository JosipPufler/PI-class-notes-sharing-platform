package hr.algebra.pi.services;

import hr.algebra.pi.models.Group;
import hr.algebra.pi.models.User;

import java.util.Date;
import java.util.List;

public class GroupBuilder {
    private final Group group;

    private GroupBuilder() {
        group = new Group();
    }

    public static GroupBuilder start() {
        return new GroupBuilder();
    }

    public GroupBuilder id(Long id) {
        group.setId(id);
        return this;
    }

    public GroupBuilder name(String name) {
        group.setName(name);
        return this;
    }

    public GroupBuilder description(String description) {
        group.setDescription(description);
        return this;
    }

    public GroupBuilder dateCreation(Date dateCreation) {
        group.setDateCreation(dateCreation);
        return this;
    }

    public GroupBuilder users(List<User> users) {
        group.setUsers(users);
        return this;
    }

    public Group build() {
        return group;
    }
}

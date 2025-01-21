package hr.algebra.pi.services;

import hr.algebra.pi.models.CreateGroupForm;
import hr.algebra.pi.models.Group;

public class GroupFactory {
    public static Group createGroup(CreateGroupForm form) {
        return new Group(form);
    }
}

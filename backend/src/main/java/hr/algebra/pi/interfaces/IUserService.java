package hr.algebra.pi.interfaces;

import hr.algebra.pi.models.User;

public interface IUserService extends IDatabaseService<User> {
    User findByUsername(String username);
    Boolean deactivateUser(Long id);
}

package hr.algebra.pi.services;

import hr.algebra.pi.models.User;

public class UserBuilder {
    private final User user = new User();

    private UserBuilder() {}

    public static UserBuilder start(){
        return new UserBuilder();
    }

    public UserBuilder id(Long id){
        user.setId(id);
        return this;
    }

    public UserBuilder username(String username){
        user.setUsername(username);
        return this;
    }

    public UserBuilder firstName(String firstName) {
        user.setFirstName(firstName);
        return this;
    }

    public UserBuilder lastName(String lastName) {
        user.setLastName(lastName);
        return this;
    }

    public UserBuilder email(String email) {
        user.setEmail(email);
        return this;
    }

    public UserBuilder passwordHash(String password) {
        user.setPasswordHash(password);
        return this;
    }

    public UserBuilder phoneNumber(String passwordHash) {
        user.setPhoneNumber(passwordHash);
        return this;
    }

    public UserBuilder active(Boolean active) {
        user.setActive(active);
        return this;
    }

    public UserBuilder settings(String settings) {
        user.setSettings(settings);
        return this;
    }

    public User build(){
        return user;
    }
}

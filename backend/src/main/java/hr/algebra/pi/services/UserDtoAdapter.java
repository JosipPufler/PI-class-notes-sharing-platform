package hr.algebra.pi.services;

import hr.algebra.pi.models.DTOs.UserDTO;
import hr.algebra.pi.models.Interest;
import hr.algebra.pi.models.User;
import hr.algebra.pi.models.UserSettings;

import java.util.HashSet;
import java.util.List;

public class UserDtoAdapter extends UserDTO {

    private final User user;
    public UserDtoAdapter(User user){
        this.user = user;
    }

    @Override
    public Long getId() {
        return user.getId();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public String getFirstName() {
        return user.getFirstName();
    }

    @Override
    public String getLastName() {
        return user.getLastName();
    }

    @Override
    public String getEmail() {
        return user.getEmail();
    }

    @Override
    public String getPhoneNumber() {
        return user.getPhoneNumber();
    }

    @Override
    public List<Interest> getInterests() {
        return user.getInterests().stream().toList();
    }

    @Override
    public Boolean getActive() {
        return user.isActive();
    }

    @Override
    public UserSettings getSettings() {
        return Mapper.jsonToUserSettings(user.getSettings());
    }

    @Override
    public void setId(Long id) {
        user.setId(id);
    }

    @Override
    public void setUsername(String username) {
        user.setUsername(username);
    }

    @Override
    public void setFirstName(String firstName) {
        user.setFirstName(firstName);
    }

    @Override
    public void setLastName(String lastName) {
        user.setLastName(lastName);
    }

    @Override
    public void setEmail(String email) {
        user.setEmail(email);
    }

    @Override
    public void setPhoneNumber(String phoneNumber) {
        user.setPhoneNumber(phoneNumber);
    }

    @Override
    public void setInterests(List<Interest> interests) {
        user.setInterests(new HashSet<>(interests));
    }

    @Override
    public void setActive(Boolean active) {
        user.setActive(active);
    }

    @Override
    public void setSettings(UserSettings settings) {
        if (settings == null) {
            user.setSettings(Mapper.userSettingsToJson(new UserSettings()));
        } else {
            user.setSettings(Mapper.userSettingsToJson(settings));
        }
    }
}

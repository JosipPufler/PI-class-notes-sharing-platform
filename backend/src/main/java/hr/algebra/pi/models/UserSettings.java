package hr.algebra.pi.models;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class UserSettings {
    Boolean twoFactorAuthenticationEnabled;

    public UserSettings() {
        twoFactorAuthenticationEnabled = false;
    }
}

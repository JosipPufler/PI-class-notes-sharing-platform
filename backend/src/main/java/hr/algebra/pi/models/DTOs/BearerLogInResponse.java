package hr.algebra.pi.models.DTOs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class BearerLogInResponse implements hr.algebra.pi.interfaces.LogInResponse {
    private String token;
    private String tokenType = "Bearer";

    private String username;
    private Long userId;

    public BearerLogInResponse(String accessToken, String username, Long userId) {
        this.token = accessToken;
        this.username = username;
        this.userId = userId;
    }

    @Override
    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public void setUserId(String userId) {
        this.userId = Long.parseLong(userId);
    }
}

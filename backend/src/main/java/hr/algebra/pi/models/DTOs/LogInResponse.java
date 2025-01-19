package hr.algebra.pi.models.DTOs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class LogInResponse {
    private String token;
    private String tokenType = "Bearer";

    private String username;
    private Long userId;

    public LogInResponse(String accessToken, String username, Long userId) {
        this.token = accessToken;
        this.username = username;
        this.userId = userId;
    }
}

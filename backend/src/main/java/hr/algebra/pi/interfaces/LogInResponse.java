package hr.algebra.pi.interfaces;

public interface LogInResponse {
    String getUsername();
    String getToken();
    String getTokenType();
    Long getUserId();

    void setUsername(String username);
    void setToken(String token);
    void setUserId(String userId);
}

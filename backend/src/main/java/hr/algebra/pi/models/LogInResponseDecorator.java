package hr.algebra.pi.models;

import hr.algebra.pi.interfaces.LogInResponse;
import hr.algebra.pi.models.DTOs.BearerLogInResponse;

public class LogInResponseDecorator {
    private final hr.algebra.pi.interfaces.LogInResponse logInResponse;

    public LogInResponseDecorator(LogInResponse logInResponse){
        this.logInResponse = logInResponse;
    }

    public String getAuthorizationHeader(){
        return logInResponse.getTokenType() + " " + logInResponse.getToken();
    }
}

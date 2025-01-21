package hr.algebra.pi.models;

import hr.algebra.pi.interfaces.LogInResponse;

public class LogInResponseDecorator {
    private final LogInResponse logInResponse;

    public LogInResponseDecorator(LogInResponse logInResponse){
        this.logInResponse = logInResponse;
    }

    public String getAuthorizationHeader(){
        return logInResponse.getTokenType() + " " + logInResponse.getToken();
    }
}

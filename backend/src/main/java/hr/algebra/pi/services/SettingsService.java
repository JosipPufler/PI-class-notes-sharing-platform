package hr.algebra.pi.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import hr.algebra.pi.models.UserSettings;
import org.springframework.stereotype.Service;

@Service
public class SettingsService {
    private SettingsService(){}

    static ObjectMapper objectMapper = new ObjectMapper();

    public static String UserSettingsToJson(UserSettings userSettings){
        try {
            return objectMapper.writeValueAsString(userSettings);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public static UserSettings JsonToUserSettings(String json){
        try {
            return objectMapper.readValue(json, UserSettings.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}

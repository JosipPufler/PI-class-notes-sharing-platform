package hr.algebra.pi.models.DTOs;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class LogInForm {
    String username;
    String password;
}

package hr.algebra.pi.models.DTOs;

import hr.algebra.pi.models.Interest;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InterestDTO {
    Long id;
    String name;
    String parentInterest;
}

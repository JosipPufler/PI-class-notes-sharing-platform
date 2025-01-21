package hr.algebra.pi.models;

import lombok.Setter;

public class CreateGroupForm {
    @Setter
    String name;
    String description;

    public CreateGroupForm(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

}

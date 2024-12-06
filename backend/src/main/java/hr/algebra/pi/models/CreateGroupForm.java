package hr.algebra.pi.models;

public class CreateGroupForm {
    String name;
    String description;

    CreateGroupForm(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "CreateGroupForm{" + "name=" + name + ", description=" + description + '}';
    }
}

package hr.algebra.pi.models;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name="Groups")
public class Group {
    @Id
    @Column(name="idgroup", unique=true, nullable=false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name="name", unique=true, nullable=false)
    String name;
    @Column(name="description")
    String description;
    @Column(name = "date_creation")
    Date dateCreation;

    public Group() {}

    public Group(Long id, String name, String description, Date dateCreation) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.dateCreation = dateCreation;
    }

    public Group(CreateGroupForm createGroupForm) {
        this.name = createGroupForm.getName();
        this.description = createGroupForm.getDescription();
        this.dateCreation = new Date();
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setDateCreation(Date dateCreation) {
        this.dateCreation = dateCreation;
    }

    public Date getDateCreation() {
        return dateCreation;
    }

    @Override
    public String toString() {
        return "Group{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", dateCreation=" + dateCreation +
                '}';
    }
}

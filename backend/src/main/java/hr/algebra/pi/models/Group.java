package hr.algebra.pi.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
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
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "GroupUser",
            joinColumns = @JoinColumn(name = "group_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private List<User> users = new ArrayList<>();;

    public Group() {}


    public Group(CreateGroupForm createGroupForm) {
        this.name = createGroupForm.getName();
        this.description = createGroupForm.getDescription();
        this.dateCreation = new Date();
        this.users = new ArrayList<>();
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

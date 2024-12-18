package hr.algebra.pi.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Setter
@Getter
@Entity
@AllArgsConstructor
@Table(name="Interests")
@NoArgsConstructor
public class Interest {
    @Id
    @Column(name="id", unique=true, nullable=false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name="name", unique=true, nullable=false)
    public String name;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parentInterestId")
    public Interest parentInterest;


    @ManyToMany(mappedBy = "UserInterests")
    public Set<User> interestedUsers;
}

package hr.algebra.pi.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name= "authentication_tokens", schema = "public")
public class TwoFactorAuthenticationEntry {
    @Id
    @Column(name="id", unique=true, nullable=false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @Column(name="code", unique=true, nullable=false)
    String code;
    @Column(name="expiryDate", nullable=false)
    LocalDateTime expiryDateTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId", nullable = false)
    User user;
}

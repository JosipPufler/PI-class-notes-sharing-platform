package hr.algebra.pi.repositories;

import hr.algebra.pi.models.TwoFactorAuthenticationEntry;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TwoFARepo extends JpaRepository<TwoFactorAuthenticationEntry, Long> {
    default Optional<TwoFactorAuthenticationEntry> getEntryByUserId(Long userId){
        List<TwoFactorAuthenticationEntry> all = this.findAll();
        for(TwoFactorAuthenticationEntry entry : all){
            if (entry.getUser().getId().equals(userId)){
                return Optional.of(entry);
            }
        }
        return Optional.empty();
    }

    default void deleteEntryByUserId(Long userId){
        List<TwoFactorAuthenticationEntry> all = this.findAll();
        for(TwoFactorAuthenticationEntry entry : all){
            if (entry.getUser().getId().equals(userId)){
                delete(entry);
            }
        }
        flush();
    }
}

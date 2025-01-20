package hr.algebra.pi.services;

import hr.algebra.pi.models.TwoFactorAuthenticationEntry;
import hr.algebra.pi.repositories.TwoFARepo;
import hr.algebra.pi.interfaces.IDatabaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Component
@Transactional
public class TwoFAService implements IDatabaseService<TwoFactorAuthenticationEntry> {
    TwoFARepo repo;

    @Autowired
    public TwoFAService(TwoFARepo repo) {
        this.repo = repo;
    }

    @Override
    public TwoFactorAuthenticationEntry findById(Long id) {
        return repo.findById(id).orElseThrow(() -> new RuntimeException("Entry not found"));
    }

    @Override
    public void update(TwoFactorAuthenticationEntry entity) {
        repo.saveAndFlush(entity);
    }

    @Override
    public void deleteById(Long id) {
        repo.deleteById(id);
    }

    public List<TwoFactorAuthenticationEntry> getAll(){
        return repo.findAll();
    }

    @Override
    public TwoFactorAuthenticationEntry create(TwoFactorAuthenticationEntry entity) {
        deleteAllUserEntries(entity.getUser().getId());
        return repo.saveAndFlush(entity);
    }

    public Optional<TwoFactorAuthenticationEntry> getByUserId(Long userId) {
        Optional<TwoFactorAuthenticationEntry> twoFactorAuthenticationEntry = repo.getEntryByUserId(userId);
        if (twoFactorAuthenticationEntry.isPresent() && twoFactorAuthenticationEntry.get().getExpiryDateTime().isAfter(LocalDateTime.now())) {
            return twoFactorAuthenticationEntry;
        }
        deleteAllUserEntries(userId);
        return Optional.empty();
    }

    public void deleteAllUserEntries(Long userId) {
        repo.deleteEntryByUserId(userId);
    }
}

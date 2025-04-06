package repository;

import models.PasswordResetToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Optional;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {

    // Find token by value (for password reset)
    Optional<PasswordResetToken> findByToken(String token);

    // Delete expired tokens (scheduled cleanup)
    void deleteByExpiryDateBefore(LocalDateTime now);
}
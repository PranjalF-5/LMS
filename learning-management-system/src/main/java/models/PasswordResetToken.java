package models;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Data
@Builder
public class PasswordResetToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String token; // Unique reset token

    private LocalDateTime expiryDate; // Token expiration time

    // Relationships
    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user; // User requesting reset
}

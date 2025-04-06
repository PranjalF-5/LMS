package models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApprovalRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long requestId;

    @Enumerated(EnumType.STRING)
    private ApprovalStatus status; // APPROVED/REJECTED/PENDING

    @CreationTimestamp
    private LocalDateTime requestDate; // Auto-populated

    private LocalDateTime decisionDate; // Set when admin acts

    // Relationships
    @ManyToOne
    @JoinColumn(name = "admin_id", nullable = false)
    private User admin; // ADMIN who approved/rejected

    @ManyToOne
    @JoinColumn(name = "course_id", nullable = false)
    private Course course; // Course being approved
}

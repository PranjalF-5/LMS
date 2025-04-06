package models;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;

@Entity
@Data
@Builder
public class Enrollment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long enrollmentId;

    @CreationTimestamp
    private LocalDateTime enrolledAt; // Auto-populated

    // Relationships
    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    private User student; // Enrolled student

    @ManyToOne
    @JoinColumn(name = "course_id", nullable = false)
    private Course course; // Enrolled course
}

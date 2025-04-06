package models;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.UpdateTimestamp;
import java.time.LocalDateTime;

@Entity
@Data
public class Syllabus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long syllabusId;

    @Lob
    private String content;

    @UpdateTimestamp
    private LocalDateTime lastUpdated;

    @OneToOne
    @JoinColumn(name = "course_id")
    private Course course;
}

package service;

import com.pranjal.learning_management_system.dto.SyllabusUpdateDTO;
import com.pranjal.learning_management_system.dto.SyllabusResponseDTO;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import models.Course;
import models.Syllabus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import repository.CourseRepository;
import repository.SyllabusRepository;
import java.time.LocalDateTime;

/**
 * Service class for handling syllabus-related business logic.
 * Manages syllabus creation, updates, and retrieval operations.
 */
@Service
@RequiredArgsConstructor
public class SyllabusService {

    private final SyllabusRepository syllabusRepository;
    private final CourseRepository courseRepository;

    /**
     * Updates the syllabus content for a specific course.
     * Creates a new syllabus if one doesn't exist.
     * 
     * @param courseId The ID of the course whose syllabus is to be updated
     * @param updateDTO The new syllabus content
     * @return The syllabus response DTO containing the updated syllabus details
     * @throws RuntimeException if the course is not found
     * @throws IllegalArgumentException if the content is empty
     */
    @Transactional
    public SyllabusResponseDTO updateSyllabus(Long courseId, SyllabusUpdateDTO updateDTO) {
        // Validate the content
        if (updateDTO.getContent() == null || updateDTO.getContent().trim().isEmpty()) {
            throw new IllegalArgumentException("Syllabus content cannot be empty");
        }

        // Find the course
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));

        // Find existing syllabus or create new one
        Syllabus syllabus = syllabusRepository.findByCourse(course)
                .orElse(new Syllabus());

        // Update syllabus content
        syllabus.setCourse(course);
        syllabus.setContent(updateDTO.getContent());
        syllabus.setLastUpdated(LocalDateTime.now());
        syllabus = syllabusRepository.save(syllabus);

        // Build and return the response DTO
        return convertToResponseDTO(syllabus);
    }

    /**
     * Retrieves the syllabus for a specific course.
     * 
     * @param courseId The ID of the course whose syllabus is to be retrieved
     * @return The syllabus response DTO containing the syllabus details
     * @throws RuntimeException if the course or syllabus is not found
     */
    public SyllabusResponseDTO getSyllabusByCourse(Long courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new EntityNotFoundException("Course not found"));
        Syllabus syllabus = syllabusRepository.findByCourse(course)
                .orElseThrow(() -> new EntityNotFoundException("Syllabus not found"));
        return convertToResponseDTO(syllabus);
    }

    private SyllabusResponseDTO convertToResponseDTO(Syllabus syllabus) {
        return new SyllabusResponseDTO(
            syllabus.getSyllabusId(),
            syllabus.getContent(),
            syllabus.getLastUpdated(),
            syllabus.getCourse().getCourseId(),
            syllabus.getCourse().getName()
        );
    }
}

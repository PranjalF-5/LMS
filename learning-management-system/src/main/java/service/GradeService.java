package service;

import com.pranjal.learning_management_system.dto.GradeDTO;
import com.pranjal.learning_management_system.dto.GradeResponseDTO;
import com.pranjal.learning_management_system.dto.GradeViewDTO;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import models.Course;
import models.Grade;
import models.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import repository.CourseRepository;
import repository.GradeRepository;
import repository.UserRepository;

import java.util.List;

/**
 * Service class for handling grade-related business logic.
 * Manages grade assignment and validation operations.
 */
@Service
@RequiredArgsConstructor
@Transactional
public class GradeService {

    private final GradeRepository gradeRepository;
    private final UserRepository userRepository;
    private final CourseRepository courseRepository;

    /**
     * Constructor for GradeService.
     * 
     * @param gradeRepository Repository for grade data access
     * @param userRepository Repository for user data access
     * @param courseRepository Repository for course data access
     */
    public GradeService(GradeRepository gradeRepository, UserRepository userRepository, CourseRepository courseRepository) {
        this.gradeRepository = gradeRepository;
        this.userRepository = userRepository;
        this.courseRepository = courseRepository;
    }

    /**
     * Assigns a grade to a student for a specific course.
     * 
     * @param gradeDTO The grade details including student, course, and score
     * @return The grade response DTO containing the assigned grade details
     * @throws RuntimeException if the student or course is not found
     * @throws IllegalArgumentException if the score is invalid
     */
    @Transactional
    public GradeResponseDTO assignGrade(GradeDTO gradeDTO) {
        // Validate the score
        validateScore(gradeDTO.getScore());

        // Find the student and course
        User student = userRepository.findById(gradeDTO.getStudentId())
                .orElseThrow(() -> new RuntimeException("Student not found"));
        Course course = courseRepository.findById(gradeDTO.getCourseId())
                .orElseThrow(() -> new RuntimeException("Course not found"));

        // Check if grade already exists for this student & course
        Grade grade = gradeRepository.findByStudentAndCourse(student, course)
                .map(existingGrade -> {
                    existingGrade.setScore(gradeDTO.getScore());
                    return existingGrade;
                })
                .orElseGet(() -> Grade.builder()
                        .student(student)
                        .course(course)
                        .score(gradeDTO.getScore())
                        .build());

        Grade savedGrade = gradeRepository.save(grade);

        return convertToResponseDTO(savedGrade);
    }

    /**
     * Validates that a score is within the acceptable range (0-100).
     * 
     * @param score The score to validate
     * @throws IllegalArgumentException if the score is outside the valid range
     */
    private void validateScore(Integer score) {
        if (score < 0 || score > 100) {
            throw new IllegalArgumentException("Score must be between 0 and 100");
        }
    }

    public GradeViewDTO getGradeByStudentAndCourse(User student, Long courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new EntityNotFoundException("Course not found"));
        Grade grade = gradeRepository.findByStudentAndCourse(student, course)
                .orElseThrow(() -> new EntityNotFoundException("Grade not found"));
        return convertToViewDTO(grade);
    }

    public List<Grade> getGradesByCourse(Long courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new EntityNotFoundException("Course not found"));
        return gradeRepository.findByCourse(course);
    }

    private GradeResponseDTO convertToResponseDTO(Grade grade) {
        return new GradeResponseDTO(
                grade.getGradeId(),
                grade.getScore(),
                grade.getStudent().getUserId(),
                grade.getStudent().getUsername(),
                grade.getCourse().getCourseId(),
                grade.getCourse().getName()
        );
    }

    private GradeViewDTO convertToViewDTO(Grade grade) {
        return GradeViewDTO.builder()
                .gradeId(grade.getGradeId())
                .score(grade.getScore())
                .studentId(grade.getStudent().getUserId())
                .studentName(grade.getStudent().getUsername())
                .courseId(grade.getCourse().getCourseId())
                .courseName(grade.getCourse().getName())
                .build();
    }
}

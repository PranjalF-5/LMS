package service;

import com.pranjal.learning_management_system.dto.CourseRequestDTO;
import jakarta.persistence.EntityNotFoundException;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import models.ApprovalRequest;
import models.ApprovalStatus;
import models.Course;
import models.CourseStatus;
import models.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import repository.ApprovalRequestRepository;
import repository.CourseRepository;
import repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Service class for handling course-related business logic.
 * Manages course creation, status updates, and retrieval operations.
 */
@Service
@RequiredArgsConstructor
@Builder
@Transactional
public class CourseService {

    private final CourseRepository courseRepository;
    private final UserRepository userRepository;
    private final ApprovalRequestRepository approvalRequestRepository;

    /**
     * Creates a new course for an instructor.
     * 
     * @param courseDTO The course details to be created
     * @param instructorId The ID of the instructor creating the course
     * @return The created course entity
     * @throws RuntimeException if the instructor is not found
     */
    @Transactional
    public Course createCourse(CourseRequestDTO courseDTO, Long instructorId) {
        // Find the instructor by ID
        User instructor = userRepository.findById(instructorId)
                .orElseThrow(() -> new RuntimeException("Instructor not found"));

        // Create and save the new course
        Course course = Course.builder()
                .name(courseDTO.getCourseName())
                .description(courseDTO.getDescription())
                .instructor(instructor)
                .status(CourseStatus.PENDING)
                .build();

        return courseRepository.save(course);
    }

    /**
     * Updates the status of a course.
     * 
     * @param courseId The ID of the course to update
     * @param status The new status to set
     * @return The updated course entity
     * @throws RuntimeException if the course is not found
     */
    @Transactional
    public Course updateCourseStatus(Long courseId, CourseStatus status, User admin) {
        // Find the course by ID
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new EntityNotFoundException("Course not found"));

        // Update and save the course status
        course.setStatus(status);

        ApprovalRequest approvalRequest = ApprovalRequest.builder()
                .admin(admin)
                .course(course)
                .status(status == CourseStatus.APPROVED ? ApprovalStatus.APPROVED : ApprovalStatus.REJECTED)
                .decisionDate(LocalDateTime.now())
                .build();

        approvalRequestRepository.save(approvalRequest);
        return courseRepository.save(course);
    }

    /**
     * Retrieves all courses with a specific status.
     * 
     * @param status The status to filter courses by
     * @return List of courses with the specified status
     */
    public List<Course> getCoursesByStatus(CourseStatus status) {
        return courseRepository.findByStatus(status);
    }

    /**
     * Retrieves a course by its ID.
     * 
     * @param courseId The ID of the course to retrieve
     * @return The course entity
     * @throws RuntimeException if the course is not found
     */
    public Course getCourseById(Long courseId) {
        return courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));
    }
}

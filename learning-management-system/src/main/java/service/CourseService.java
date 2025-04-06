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

@Service
@RequiredArgsConstructor
@Builder
@Transactional
public class CourseService {

    private final CourseRepository courseRepository;
    private final UserRepository userRepository;
    private final ApprovalRequestRepository approvalRequestRepository;

    @Transactional
    public Course createCourse(CourseRequestDTO courseDTO, Long instructorId) {
        // Re-fetch the instructor inside a transactional context
        User instructor = userRepository.findById(instructorId)
                .orElseThrow(() -> new RuntimeException("Instructor not found"));

        Course course = Course.builder()
                .name(courseDTO.getCourseName())
                .description(courseDTO.getDescription())
                .instructor(instructor)
                .status(CourseStatus.PENDING)
                .build();

        return courseRepository.save(course);
    }

    @Transactional
    public Course updateCourseStatus(Long courseId, CourseStatus status, User admin) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new EntityNotFoundException("Course not found"));
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

    public List<Course> getCoursesByStatus(CourseStatus status) {
        return courseRepository.findByStatus(status);
    }
}

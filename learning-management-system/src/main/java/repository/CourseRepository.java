package repository;

import models.Course;
import models.CourseStatus;
import models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CourseRepository extends JpaRepository<Course, Long> {

    // Find courses by approval status (e.g., "APPROVED")
    List<Course> findByStatus(CourseStatus status);

    // Find courses created by a specific instructor
    List<Course> findByInstructor(User instructor);
}
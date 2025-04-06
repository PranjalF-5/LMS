package repository;

import models.Course;
import models.Enrollment;
import models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {

    // Find enrollments for a student
    List<Enrollment> findByStudent(User student);

    // Find enrollments for a course
    List<Enrollment> findByCourse(Course course);
}
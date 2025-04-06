package repository;

import models.Course;
import models.Grade;
import models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface GradeRepository extends JpaRepository<Grade, Long> {

    // We assume one grade per student per course, so this should return optional
    Optional<Grade> findByStudentAndCourse(User student, Course course);

    List<Grade> findByCourse(Course course);
}

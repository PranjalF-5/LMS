package repository;

import models.Course;
import models.Syllabus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SyllabusRepository extends JpaRepository<Syllabus, Long> {

    // Find syllabus by course
    Optional<Syllabus> findByCourse(Course course);
}
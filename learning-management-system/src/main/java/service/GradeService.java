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

@Service
@RequiredArgsConstructor
@Transactional
public class GradeService {

    private final GradeRepository gradeRepository;
    private final UserRepository userRepository;
    private final CourseRepository courseRepository;

    @Transactional
    public GradeResponseDTO assignGrade(GradeDTO gradeDTO) {
        User student = userRepository.findById(gradeDTO.getStudentId())
                .orElseThrow(() -> new EntityNotFoundException("Student not found"));
        Course course = courseRepository.findById(gradeDTO.getCourseId())
                .orElseThrow(() -> new EntityNotFoundException("Course not found"));

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

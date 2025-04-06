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

@Service
@RequiredArgsConstructor
public class SyllabusService {

    private final SyllabusRepository syllabusRepository;
    private final CourseRepository courseRepository;

    @Transactional
    public SyllabusResponseDTO updateSyllabus(Long courseId, SyllabusUpdateDTO updateDTO) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new EntityNotFoundException("Course not found"));

        Syllabus syllabus = syllabusRepository.findByCourse(course)
                .orElse(new Syllabus());

        syllabus.setContent(updateDTO.getContent());
        syllabus.setLastUpdated(LocalDateTime.now());
        syllabus.setCourse(course);

        Syllabus savedSyllabus = syllabusRepository.save(syllabus);
        return convertToResponseDTO(savedSyllabus);
    }

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

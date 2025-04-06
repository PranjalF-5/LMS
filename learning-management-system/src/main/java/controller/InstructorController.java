package controller;

import com.pranjal.learning_management_system.dto.CourseRequestDTO;
import com.pranjal.learning_management_system.dto.GradeDTO;
import com.pranjal.learning_management_system.dto.GradeResponseDTO;
import com.pranjal.learning_management_system.dto.SyllabusUpdateDTO;
import com.pranjal.learning_management_system.dto.SyllabusResponseDTO;
import com.pranjal.learning_management_system.utils.CustomUserDetails;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import models.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import service.CourseService;
import service.GradeService;
import service.SyllabusService;

@RestController
@RequestMapping("/api/instructor")
@RequiredArgsConstructor
@PreAuthorize("hasRole('INSTRUCTOR')")
public class InstructorController {

    private final CourseService courseService;
    private final GradeService gradeService;
    private final SyllabusService syllabusService;

    // Create a new course
    @PostMapping("/courses")
    public ResponseEntity<CourseDao> createCourse(
            @Valid @RequestBody CourseRequestDTO courseDTO,
            @AuthenticationPrincipal CustomUserDetails customUserDetails // Use custom UserDetails
    ) {
        User instructor = customUserDetails.getUser(); // Get actual User entity
        Course course = courseService.createCourse(courseDTO, instructor.getUserId());
        CourseDao courseDao = CourseDao.builder().instructorId(course.getInstructor().getUserId()).courseId(course.getCourseId()).description(course.getDescription()).name(course.getName()).status(course.getStatus()).build();
        return ResponseEntity.status(HttpStatus.CREATED).body(courseDao);
    }

    // Grade a student
    @PostMapping("/grade")
    public ResponseEntity<GradeResponseDTO> assignGrade(@Valid @RequestBody GradeDTO gradeDTO) {
        GradeResponseDTO response = gradeService.assignGrade(gradeDTO);
        return ResponseEntity.ok(response);
    }

    // Update syllabus content
    @PutMapping("/syllabus/{courseId}")
    public ResponseEntity<SyllabusResponseDTO> updateSyllabus(
            @PathVariable Long courseId,
            @Valid @RequestBody SyllabusUpdateDTO updateDTO
    ) {
        SyllabusResponseDTO syllabus = syllabusService.updateSyllabus(courseId, updateDTO);
        return ResponseEntity.ok(syllabus);
    }
}

@Builder
@Getter
@Setter
class CourseDao {


    private long courseId;

    private String name;

    private String description;

    private CourseStatus status;

    private Long instructorId;
}

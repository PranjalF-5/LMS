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

/**
 * Controller class for handling instructor-related operations.
 * All endpoints are secured and require INSTRUCTOR role.
 */
@RestController
@RequestMapping("/api/instructor")
@RequiredArgsConstructor
@PreAuthorize("hasRole('INSTRUCTOR')")
public class InstructorController {

    // Service dependencies for handling business logic
    private final CourseService courseService;
    private final GradeService gradeService;
    private final SyllabusService syllabusService;

    /**
     * Creates a new course for the authenticated instructor.
     * 
     * @param courseDTO The course details to be created
     * @param customUserDetails The authenticated instructor's details
     * @return ResponseEntity containing the created course details
     */
    @PostMapping("/courses")
    public ResponseEntity<CourseDao> createCourse(
            @Valid @RequestBody CourseRequestDTO courseDTO,
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {
        // Get the instructor entity from the authenticated user details
        User instructor = customUserDetails.getUser();
        
        // Create the course using the service layer
        Course course = courseService.createCourse(courseDTO, instructor.getUserId());
        
        // Build the response DTO
        CourseDao courseDao = CourseDao.builder()
                .instructorId(course.getInstructor().getUserId())
                .courseId(course.getCourseId())
                .description(course.getDescription())
                .name(course.getName())
                .status(course.getStatus())
                .build();
                
        // Return the created course with 201 CREATED status
        return ResponseEntity.status(HttpStatus.CREATED).body(courseDao);
    }

    /**
     * Assigns a grade to a student for a specific course.
     * 
     * @param gradeDTO The grade details including student, course, and score
     * @return ResponseEntity containing the assigned grade details
     */
    @PostMapping("/grade")
    public ResponseEntity<GradeResponseDTO> assignGrade(@Valid @RequestBody GradeDTO gradeDTO) {
        // Assign grade using the service layer
        GradeResponseDTO response = gradeService.assignGrade(gradeDTO);
        
        // Return the assigned grade with 200 OK status
        return ResponseEntity.ok(response);
    }

    /**
     * Updates the syllabus content for a specific course.
     * 
     * @param courseId The ID of the course whose syllabus is to be updated
     * @param updateDTO The new syllabus content
     * @return ResponseEntity containing the updated syllabus details
     */
    @PutMapping("/syllabus/{courseId}")
    public ResponseEntity<SyllabusResponseDTO> updateSyllabus(
            @PathVariable Long courseId,
            @Valid @RequestBody SyllabusUpdateDTO updateDTO
    ) {
        // Update syllabus using the service layer
        SyllabusResponseDTO syllabus = syllabusService.updateSyllabus(courseId, updateDTO);
        
        // Return the updated syllabus with 200 OK status
        return ResponseEntity.ok(syllabus);
    }
}

/**
 * Data Transfer Object for course-related responses.
 * Used to transfer course data between the controller and client.
 */
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


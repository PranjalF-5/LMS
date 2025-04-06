package controller;

import com.pranjal.learning_management_system.dto.EnrollmentResponseDTO;
import com.pranjal.learning_management_system.dto.SyllabusDTO;
import com.pranjal.learning_management_system.dto.SyllabusResponseDTO;
import com.pranjal.learning_management_system.utils.CustomUserDetails;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.websocket.server.PathParam;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import models.Enrollment;
import models.Grade;
import models.Syllabus;
import models.User;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import service.EnrollmentService;
import service.GradeService;
import service.SyllabusService;
import com.pranjal.learning_management_system.dto.GradeViewDTO;


@RestController
@RequestMapping("/api/student")
@RequiredArgsConstructor
@PreAuthorize("hasRole('STUDENT')")
@Builder
public class StudentController {

    private final EnrollmentService enrollmentService;
    private final GradeService gradeService;
    private final SyllabusService syllabusService;

    // Enroll in a course
    @PostMapping("/enroll/{courseId}")
    public ResponseEntity<EnrollmentResponseDTO> enrollCourse(
            @PathVariable("courseId")
            Long courseId,
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {
        User student = customUserDetails.getUser();
        Enrollment enrollment = enrollmentService.enrollStudent(student, courseId);
        EnrollmentResponseDTO response = EnrollmentResponseDTO.builder()
                .enrollmentId(enrollment.getEnrollmentId())
                .courseName(enrollment.getCourse().getName())
                .enrolledAt(enrollment.getEnrolledAt())
                .build();
        return ResponseEntity.ok(response);
    }

    // View grades for a course
    @GetMapping("/grades/{courseId}")
    public ResponseEntity<GradeViewDTO> viewGrades(
            @PathVariable("courseId") Long courseId,
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {
        User student = customUserDetails.getUser();
        GradeViewDTO grade = gradeService.getGradeByStudentAndCourse(student, courseId);
        return ResponseEntity.ok(grade);
    }

    // View syllabus for a course
    @GetMapping("/syllabus/{courseId}")
    public ResponseEntity<SyllabusDTO> viewSyllabus(@PathVariable Long courseId) {
        SyllabusResponseDTO syllabusResponse = syllabusService.getSyllabusByCourse(courseId);
        SyllabusDTO response = SyllabusDTO.builder()
                .content(syllabusResponse.getContent())
                .lastUpdated(syllabusResponse.getLastUpdated())
                .build();
        return ResponseEntity.ok(response);
    }
}
